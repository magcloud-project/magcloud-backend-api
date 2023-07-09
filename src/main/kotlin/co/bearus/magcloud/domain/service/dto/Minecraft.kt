//package co.bearus.magcloud.domain.service.dto
//
//abstract class View {
//    abstract fun build(): ContainerContext
//}
//
//class MenuView(
//    private val menuViewModel: MenuViewModel
//): View() {
//    override fun build(): ContainerContext {
//        return Container(title = "나의 작은 상자", size = 9,) {
//            for(i in 0..10) {
//
//            }
//            ContainerButton {
//                exposedSlot = 3
//                action = menuViewModel::onMainButtonPress
//                name = "메인 버튼"
//            }
//
//            ContainerButton {
//                exposedSlot = 5
//                action = menuViewModel::onTestButtonPress
//                name = "테스트 버튼"
//            }
//
//            ContainerButton {
//
//            }
//        }
//    }
//}
//
//class MenuViewModel {
//    fun onMainButtonPress() {
//        println("버튼이 눌렸어용")
//    }
//
//    fun onTestButtonPress() {
//        println("테스트 버튼이 눌렸어용")
//    }
//}
//
//fun Container(
//    title: String = "Inventory",
//    size: Int = 9,
//    lambda: ContainerContext.() -> Unit
//): ContainerContext {
//    val context = ContainerContext()
//    lambda(context)
//    return context
//}
//
//class ContainerContext {
//    val elements: MutableList<ButtonContext> = mutableListOf()
//
//    fun ContainerButton(
//
//        lambda: ButtonContext.() -> Unit,
//    ) {
//        val newContext = ButtonContext()
//        elements.add(newContext)
//        lambda(newContext)
//    }
//}
//
//
//
//class ButtonContext {
//    var exposedSlot: Int = 0
//    var action: () -> Unit = {}
//    var name: String = ""
//}
