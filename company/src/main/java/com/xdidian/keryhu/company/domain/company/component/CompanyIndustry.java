package com.xdidian.keryhu.company.domain.company.component;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by hushuming on 2016/9/23.
 * <p>
 * 主要的 行业分类。
 *
 *  只有在前台需要 中文显示的时候，才会将enum按照这种格式，这样的好处
 *  后台可以直接将中文传递给前台，前台选择哪个enum的值，直接对应中文名字，传递给后台。
 *  节省了，前后台，中英文之间的转换
 *
 *
 */


public enum CompanyIndustry {

    NONG_LIN("农、林、牧、渔业"),
    CAI_KUANG("采矿业"),
    ZHI_ZAO("制造业"),
    DIAN_LI("电力、热力、燃气及水生产和供应业"),
    JIAN_ZU("建筑业"),          //                           5
    PI_FA("批发和零售业"),
    JIAO_TONG("交通运输、仓储和邮政业"),
    ZU_SU("住宿和餐饮业"),
    XIN_XI("信息传输、软件和信息技术服务业"),
    JIN_RONG("金融业"),                   //               10
    FANG_CHAN("房地产业"),
    ZU_LIN("租赁和商务服务业"),
    KE_YAN("科学研究和技术服务业"),
    SHUI_LI("水利、环境和公共设施管理业"),
    JU_MIN_FU_WU("居民服务、修理和其他服务业"),    //     15
    JIAO_YU("教育"),
    WEI_SHENG("卫生和社会工作"),
    WEN_HUA("文化、体育和娱乐业"),
    GONG_GONG_GUAN_LI("公共管理、社会保障和社会组织"),
    GUO_JI_ZU_ZHI("国际组织");        //    国际组织               20

    private final String value;

    CompanyIndustry(String value){
        this.value=value;
    }

    @JsonValue
    public String toValue() {
        return value;
    }

    @JsonCreator
    public static CompanyIndustry forValue(String value){
        for(CompanyIndustry c:CompanyIndustry.values()){
            if(c.value.equals(value))
                return c;
        }
        String err=new StringBuffer("您提供的值: ")
                .append(value)
                .append(" 不对！")
                .toString();

        throw new IllegalArgumentException(err);
    }



}
