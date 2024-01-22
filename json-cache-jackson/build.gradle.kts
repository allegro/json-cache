dependencies {
    api(project(":json-cache-core"))
    compileOnly(libs.jackson.databind)

    testImplementation(project(":json-cache-test"))
    testImplementation(libs.jackson.databind)
}