import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties
import net.thebugmc.gradle.sonatypepublisher.PublishingType.AUTOMATIC

plugins {
    id("com.android.library")
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.3"
}

android {
    namespace = "spa.lyh.cn.lib_https"
    compileSdk = 35


    defaultConfig {
        minSdk = 21
    }

    buildTypes {
        release {
            isMinifyEnabled  = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility =  JavaVersion.VERSION_21
    }
}

dependencies {
    api("com.squareup.okhttp3:okhttp:${Lib.okhttp}")
    api("com.alibaba.fastjson2:fastjson2:${Lib.fastjson}")
    api("io.github.liyuhaolol:IO:${Lib.lib_io}")
    implementation("org.conscrypt:conscrypt-android:${Lib.conscrypt}")
    implementation("androidx.documentfile:documentfile:${Lib.documentfile}")
}

var signingKeyId = ""//签名的密钥后8位
var signingPassword = ""//签名设置的密码
var secretKeyRingFile = ""//生成的secring.gpg文件目录
var ossrhUsername = ""//sonatype用户名
var ossrhPassword = "" //sonatype密码

val localProperties = project.rootProject.file("local.properties")

if (localProperties.exists()) {
    println("Found secret props file, loading props")
    val properties = Properties()

    InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
        properties.load(reader)
    }
    signingKeyId = properties.getProperty("signingKeyId")
    signingPassword = properties.getProperty("signingPassword")
    secretKeyRingFile = properties.getProperty("secretKeyRingFile")
    ossrhUsername = properties.getProperty("ossrhUsername")
    ossrhPassword = properties.getProperty("ossrhPassword")

} else {
    println("No props file, loading env vars")
}


centralPortal {
    username = ossrhUsername
    password = ossrhPassword
    name = "HttpUtils"
    group = "io.github.liyuhaolol"
    version = Version.versionName
    pom {
        //packaging = "aar"
        name = "HttpUtils"
        description = "Modify Okhttp"
        url = "https://github.com/liyuhaolol/HttpUtils"
        licenses {
            license {
                name = "The MIT License"
                url = "https://github.com/liyuhaolol/HttpUtils/blob/master/LICENSE"
            }
        }
        developers {
            developer {
                id = "liyuhao"
                name = "liyuhao"
                email = "liyuhaoid@sina.com"
            }
        }
        scm {
            connection = "scm:git@github.com/liyuhaolol/HttpUtils.git"
            developerConnection = "scm:git@github.com/liyuhaolol/HttpUtils.git"
            url = "https://github.com/liyuhaolol/HttpUtils"
        }

    }
    publishingType = AUTOMATIC
    javadocJarTask = tasks.create<Jar>("javadocEmptyJar") {
        archiveClassifier = "javadoc"
    }

}


gradle.taskGraph.whenReady {
    if (allTasks.any { it is Sign }) {
        allprojects {
            extra["signing.keyId"] = signingKeyId
            extra["signing.secretKeyRingFile"] = secretKeyRingFile
            extra["signing.password"] = signingPassword
        }
    }
}

signing {
    sign(publishing.publications)
}