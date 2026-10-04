package com.sample.hexagonal.splearn.domain.course

import io.kotest.core.spec.style.StringSpec

class CourseFixtureTest :
    StringSpec({
        "createCourse" {
            println(CourseFixture.createCourse())
        }
    })
