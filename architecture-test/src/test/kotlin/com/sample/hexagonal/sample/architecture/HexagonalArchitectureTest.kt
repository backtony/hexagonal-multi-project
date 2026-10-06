package com.sample.hexagonal.sample.architecture

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.library.Architectures
import com.tngtech.archunit.library.dependencies.Slice
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition
import io.kotest.core.spec.style.StringSpec

private const val BASE = "com.sample.hexagonal.sample"

class HexagonalArchitectureTest :
    StringSpec({
        val classes: JavaClasses = ClassFileImporter()
            .withImportOption(ImportOption.DoNotIncludeTests())
            .withImportOption { location -> !location.contains("testFixtures") && !location.contains("test-fixtures") }
            .importPackages(BASE)

        // 레이어 의존 방향: adapter → application → domain 으로만 흐른다
        "hexagonalArchitecture" {
            Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .layer("domain").definedBy("$BASE.domain..")
                .layer("application").definedBy("$BASE.application..")
                .layer("adapter").definedBy("$BASE.adapter..")
                .whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapter")
                .whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
                .whereLayer("adapter").mayNotBeAccessedByAnyLayer()
                .check(classes)
        }

        // domain 의 애그리거트 패키지끼리 순환 의존이 없다
        "aggregateFreeOfCycles" {
            SlicesRuleDefinition.slices().matching("$BASE.domain.(*)..").should().beFreeOfCycles().allowEmptyShould(true).check(classes)
        }

        // application 서비스의 애그리거트 패키지끼리 순환 의존이 없다
        "applicationAggregateFreeOfCycles" {
            SlicesRuleDefinition.slices().matching(
                "$BASE.application.service.(*)..",
            ).should().beFreeOfCycles().allowEmptyShould(true).check(classes)
        }

        // 애그리거트의 상태 변경은 그 애그리거트 안에서만 일어나야 한다. 다른 애그리거트의 변경 메소드·생성자를 호출하지 못하게 막는다.
        // 허용: 프로퍼티 조회(getter), ensure* 검증, 값 객체(data class)·Enum 메소드
        "aggregateDependencies" {
            SlicesRuleDefinition.slices()
                .matching("$BASE.domain.(*)..")
                .should(onlyCallGettersOrValueObjectMethodsOfOtherSlices())
                .allowEmptyShould(true)
                .check(classes)
        }
    })

private fun onlyCallGettersOrValueObjectMethodsOfOtherSlices(): ArchCondition<Slice> =
    object : ArchCondition<Slice>("다른 슬라이스의 getter 또는 값 객체(data class), Enum 메소드만 호출할 수 있다") {
        private val classesInAnySlice = mutableSetOf<JavaClass>()

        override fun init(allSlices: Collection<Slice>) {
            allSlices.forEach { classesInAnySlice.addAll(it) }
        }

        override fun check(slice: Slice, events: ConditionEvents) {
            slice.flatMap { it.codeUnitCallsFromSelf }
                .filter { call ->
                    val owner = call.targetOwner
                    owner !in slice &&
                        owner in classesInAnySlice &&
                        !call.originOwner.isAssignableTo(owner.name) &&
                        !owner.isEnum &&
                        !owner.isDataClass() &&
                        !call.name.let { it.startsWith("get") || it.startsWith("is") || it.startsWith("ensure") }
                }
                .forEach { events.add(SimpleConditionEvent.violated(it, it.description)) }
        }
    }

private fun JavaClass.isDataClass(): Boolean = methods.any { it.name == "component1" } && methods.any { it.name == "copy" }
