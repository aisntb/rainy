fun main(){
    println("Iris server started!")
    val bot = Bot("172.25.156.225:3000")//BOT SERVER IP
    bot.addListener(MessageListener())
    bot.start()

}