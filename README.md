# rainy

<img src="https://github.com/user-attachments/assets/3f3728c5-ef3e-48f8-83c9-ed22b7f6a3c2" width="200"/>

rainy는 iris와 http로 상호작용을 하면서 카카오톡에 채팅을 주고받을 수 있게 만들어 주는 모듈입니다.
kotlin 언어로 개발되었기 때문에 java와 kotlin에서 사용가능합니다.

# Usage

setting.gradle.kts
```kts
dependencyResolutionManagement {
    // Use Maven Central as the default repository (where Gradle will download dependencies) in all subprojects.
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```
build.gradle
```kts
dependencies {
    implementation("com.github.aisntb:rainy:v1.0.3-alpha")
}
```

Main.kt
```kt
fun main(){
    val bot = Bot("172.25.156.225:3000")
    bot.addListener(MessageListener())
    println("🌈 메세지 리스너가 등록되었습니다.")
    Thread {
        bot.start()
    }.start()

    println("🌈 Rainy Server is running")
}
```

MessageListener.kt
```kt
class MessageListener: ListenerAdapter {
    override fun onMessageReceived(event: MessageEvent) {
        if (event.rawJson.user_id == "427101655") return //자기자신 메세지 무시

        println("🌦️ [${event.sender}] ${event.message}")

        if(event.message == "!텍스트"){
            event.replyText("텍스트 테스트입니다.")

        }else if(event.message == "!이미지"){
            val file = encodeImageToBase64("C:\\Users\\hayeo\\OneDrive\\그림\\favorite.png")
            event.replyImage(file)
        }else if(event.message == "!이미지들"){
            val file1 = encodeImageToBase64("C:\\Users\\hayeo\\OneDrive\\그림\\IMAGE\\1.jpg")
            val file2 = encodeImageToBase64("C:\\Users\\hayeo\\OneDrive\\그림\\IMAGE\\2.jpeg")
            val file3 = encodeImageToBase64("C:\\Users\\hayeo\\OneDrive\\그림\\IMAGE\\3.jpg")
            val base64Images = "[\"$file1\", \"$file2\", \"$file3\"]"
            event.replyImages(base64Images)
        }
    }
}

```
