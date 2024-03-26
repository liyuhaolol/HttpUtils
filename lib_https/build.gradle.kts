plugins {
    id 'com.android.library'
    id 'maven-publish'
}

android {
    namespace 'spa.lyh.cn.lib_https'
    compileSdk 34


    defaultConfig {
        minSdk 21
        targetSdk 34
        consumerProguardFiles 'consumer-rules.pro'
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    publishing {
        singleVariant('release') {
            withSourcesJar()
        }
    }
    compileOptions{
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    api "com.squareup.okhttp3:okhttp:${versions.okhttp}"
    api "com.alibaba.fastjson2:fastjson2:${versions.fastjson}"
    api "com.github.liyuhaolol:IO:${versions.lib_io}"
    implementation "org.conscrypt:conscrypt-android:${versions.conscrypt}"
    implementation "androidx.documentfile:documentfile:${versions.documentfile}"
}

afterEvaluate {
    publishing {
        publications{
            release(MavenPublication) {
                from components.release
                groupId = 'com.github.liyuhaolol'
                artifactId = 'HttpUtils'
                version = versions.versionName
            }
        }
    }
}
