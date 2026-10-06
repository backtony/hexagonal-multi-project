rootProject.name = "sample"

include("domain")
include("application")

include("adapter:inbound:controller")
include("adapter:inbound:listener")
include("adapter:inbound:event")

include("adapter:outbound:repository")
include("adapter:outbound:producer")

include("infrastructure:h2")
include("infrastructure:kafka")

include("server:api")
include("server:consumer")
include("architecture-test")

includeBuild("build-plugin")
includeBuild("common")
