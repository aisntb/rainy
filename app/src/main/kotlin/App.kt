import com.github.aisntb.rainy.Bot
import listener.MessageListener

fun main(){
    println("Iris server started!")
    val bot = Bot("172.25.156.225:3000")
    bot.addListener(MessageListener())
    bot.start()

}