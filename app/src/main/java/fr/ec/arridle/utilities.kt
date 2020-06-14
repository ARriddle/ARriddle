package fr.ec.arridle

import fr.ec.arridle.network.UserProperty


fun findUserById(list: List<UserProperty>, userId: Int?) : UserProperty?{
    return list.find {it.id == userId}
}

fun findRankById(list: List<UserProperty>, user: UserProperty?): Int {
    return list.count {it.points > (user?.points ?: 10)} + 1
}