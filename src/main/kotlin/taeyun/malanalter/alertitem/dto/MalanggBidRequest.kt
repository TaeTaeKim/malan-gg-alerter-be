package taeyun.malanalter.alertitem.dto

data class MalanggBidRequest (
    val lowPrice: Long? = null,
    val highPrice: Long? = null,
    val lowincINT: Int? = null,
    val highincINT: Int? = null,
    val lowincSTR: Int? = null,
    val highincSTR: Int? = null,
    val lowincDEX: Int? = null,
    val highincDEX: Int? = null,
    val lowincLUK: Int? = null,
    val highincLUK: Int? = null,
    val lowincPAD: Int? = null,
    val highincPAD: Int? = null,
    val lowincMAD: Int? = null,
    val highincMAD: Int? = null,
    val lowHapma: Int? = null,
    val highHapma: Int? = null,
    val lowincSpeed: Int? = null,
    val highincSpeed: Int? = null,
    val lowincACC: Int? = null,
    val highincACC: Int? = null,
    val lowincJump: Int? = null,
    val highincJump: Int? = null
){
    // 유저가 등록한 itemCondition을 메랜지지 trade request param 으로 변환
    constructor(itemCondition: ItemCondition) : this(
        lowPrice = itemCondition.lowPrice,
        highPrice = itemCondition.highPrice,
        lowincINT = itemCondition.int,
        highincINT = itemCondition.highINT?: itemCondition.int,
        lowincSTR = itemCondition.str,
        highincSTR = itemCondition.highSTR ?: itemCondition.str,
        lowincDEX = itemCondition.dex,
        highincDEX = itemCondition.highDEX ?: itemCondition.dex,
        lowincLUK = itemCondition.luk,
        highincLUK = itemCondition.highLUK ?: itemCondition.luk,
        lowincPAD = itemCondition.pad,
        highincPAD = itemCondition.highPAD ?: itemCondition.pad,
        lowincMAD = itemCondition.mad,
        highincMAD = itemCondition.highMAD ?: itemCondition.mad,
        lowHapma = itemCondition.hapma,
        highHapma = itemCondition.highHAPMA ?: itemCondition.hapma,
        lowincSpeed = itemCondition.speed,
        highincSpeed = itemCondition.highSPEED ?: itemCondition.speed,
        lowincACC = itemCondition.accuracy,
        highincACC = itemCondition.highACCURACY ?: itemCondition.accuracy,
        lowincJump = itemCondition.jump,
        highincJump = itemCondition.highJUMP ?: itemCondition.jump
    )
}