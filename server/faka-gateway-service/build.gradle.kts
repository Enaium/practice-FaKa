dependencies {

    implementation(project.extra["nacos"]!!)
    implementation(project.extra["gateway"]!!)
    implementation(project.extra["loadbalancer"]!!)

    implementation(project.extra["sa-reactor"]!!)
    implementation(project.extra["sa-redis"]!!)
    implementation(project.extra["pool"]!!)

    implementation(project.extra["doc-webflux"]!!)
    implementation(project.extra["therapi"]!!)

    kapt(project.extra["config-ap"]!!)

    implementation(project(":faka-configuration"))
    implementation(project(":faka-response-result"))
}