dependencies {
    compileOnly(project.extra["nacos"]!!)
    compileOnly(project.extra["web"]!!)
    compileOnly(project.extra["sa"]!!)
    compileOnly(project.extra["doc"]!!)
    compileOnly(project.extra["jimmer"]!!)
    compileOnly(project.extra["amqp"]!!)
    compileOnly(project(":faka-response-result"))
}