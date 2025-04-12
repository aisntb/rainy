class MessageListener : ListenerAdapter {
    override fun onMessageReceived(event: MessageEvent) {
        if (event.rawJson.user_id == "427101655") return  // 자기 자신 메시지 무시

        println("[LOG] ${event.sender} : ${event.message}")
        if(event.message == "!텍스트"){
            event.replyText("텍스트 테스트입니다.")

        }else if(event.message == "!이미지"){
            val file = encodeImageToBase64("C:\\Users\\hayeo\\OneDrive\\그림\\favorite.png")
            event.replyImage(file)

        }else if(event.message == "!이미지들"){
            val file1 = encodeImageToBase64("C:\\Users\\hayeo\\OneDrive\\그림\\IMAGE\\1.jpeg")
            val file2 = encodeImageToBase64("C:\\Users\\hayeo\\OneDrive\\그림\\IMAGE\\2.jpeg")
            val file3 = encodeImageToBase64("C:\\Users\\hayeo\\OneDrive\\그림\\IMAGE\\3.jpeg")
            val base64Images = arrayOf(file1,file2,file3)
            val jsonArray = Json.encodeToString(ListSerializer(String.serializer()), base64Images.toList())
            event.replyImages(jsonArray)
        }
    }
}


fun encodeImageToBase64(path: String): String {
    val file = File(path)
    if (!file.exists()) throw IllegalArgumentException("파일 없음: $path")

    val bytes = file.readBytes()
    return Base64.getEncoder().encodeToString(bytes)
}