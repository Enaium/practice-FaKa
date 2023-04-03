dependencies {
    implementation(project(":faka-response-result"))
    implementation(project(":faka-configuration"))
    implementation(project(":faka-dependency"))

    kapt(project.extra["config-ap"]!!)

    implementation(project.extra["alipay"]!!)
    implementation(project.extra["amqp"]!!)
}