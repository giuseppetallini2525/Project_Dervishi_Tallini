interface Element{
        fun accept(visitor: Visitor):Boolean
        var depth: Int

}