dependencies {

    api(project.extra["nacos"]!!)
    api(project.extra["web"]!!)

    api(project.extra["sa"]!!)
    api(project.extra["sa-redis"]!!)
    api(project.extra["pool"]!!)

    api(project.extra["doc"]!!)
    api(project.extra["therapi"]!!)
    api(project.extra["jimmer"]!!)

    api(project(":faka-model"))
}