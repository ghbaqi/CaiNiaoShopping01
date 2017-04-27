package com.trilink.ghbaqi.cainiaoshopping01.bean;

import java.util.List;

/**
 * Created by ghbaqi on 2017/4/23.
 */

public class Page {

    /**
     * copyright : 本API接口只允许菜鸟窝(http://www.cniao5.com)用户使用,其他机构或者个人使用均为侵权行为
     * totalCount : 23
     * currentPage : 0
     * totalPage : 0
     * pageSize : 10
     * orders : []
     * list : [{"id":12,"categoryId":1,"campaignId":6,"name":"希捷（seagate）Expansion 新睿翼1TB 2.5英寸 USB3.0 移动硬盘 (STEA1000400)","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_5519fdd0N02706f5e.jpg","price":399,"sale":402},{"id":18,"categoryId":1,"campaignId":1,"name":"华硕（ASUS）经典系列X554LP 15.6英寸笔记本 （i5-5200U 4G 500G R5-M230 1G独显 蓝牙 Win8.1 黑色）","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_5604aab9N7a91521b.jpg","price":2999,"sale":2906},{"id":29,"categoryId":1,"campaignId":13,"name":"联想（ThinkPad）E550（20DFA00RCD）15.6英寸笔记本电脑（i7-5500U 4G 500GB 2G独显 FHD高分屏 Win8.1）","imgUrl":"http://7mno4h.com2.z0.glb.qiniucdn.com/s_rmd55fbcea8N8d520a22.jpg","price":5499,"sale":6738},{"id":35,"categoryId":1,"campaignId":9,"name":"索尼 （SONY） G9 48英寸全高清 LED液晶电视 （金色）","imgUrl":"http://m.360buyimg.com/n4/jfs/t1465/56/1152971182/175914/b39652b4/55e417fcN7d7f9363.jpg!q70.jpg","price":3299,"sale":4487},{"id":45,"categoryId":1,"campaignId":2,"name":"海信（Hisense）LED58EC620UA 58英寸 炫彩4K 14核 VIDAA3智能电视(黑色)","imgUrl":"http://m.360buyimg.com/n4/jfs/t2035/113/115726791/214888/5e4203f2/55efc9b5N820416a4.jpg!q70.jpg","price":5299,"sale":3967},{"id":48,"categoryId":1,"campaignId":1,"name":"三星（SAMSUNG）UA55JU50SW 55英寸 4K超高清智能电视 黑色","imgUrl":"http://m.360buyimg.com/n4/jfs/t2110/267/571221917/150474/a38336aa/56175bc9Nc71a197b.jpg!q70.jpg","price":6399,"sale":5718},{"id":49,"categoryId":1,"campaignId":16,"name":"索尼（SONY）KDL-55R580C 55英寸 LED液晶电视（黑色）","imgUrl":"http://m.360buyimg.com/n4/jfs/t1801/106/1286076832/162742/3f43a741/55e41806N2c4924d3.jpg!q70.jpg","price":5299,"sale":8003},{"id":52,"categoryId":1,"campaignId":2,"name":"华为 Ascend Mate7 月光银 移动联通双4G手机 双卡双待双通","imgUrl":"http://m.360buyimg.com/n4/jfs/t2374/194/284133169/106220/86f4da4c/55fb5f44Na4a86b54.jpg!q70.jpg","price":2599,"sale":2920},{"id":62,"categoryId":1,"campaignId":15,"name":"【超值套装版】魅族 魅蓝2 16GB 白色 移动4G手机 双卡双待","imgUrl":"http://m.360buyimg.com/n4/jfs/t1339/47/803991388/116563/2f48981b/55e03596N65ba23ff.jpg!q70.jpg","price":768,"sale":4827},{"id":65,"categoryId":1,"campaignId":1,"name":"华为 荣耀畅玩4C 双卡双待4G手机 白色 移动4G标准版(8G ROM) 标配","imgUrl":"http://m.360buyimg.com/n4/jfs/t853/190/1078015459/198985/8802bcba/55729cc0N4bcbd173.jpg!q70.jpg","price":938,"sale":8402}]
     */

    private String copyright;
    private int         totalCount;
    private int         currentPage;
    private int         totalPage;
    private int         pageSize;
    private List<Wares> list;

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Wares> getList() {
        return list;
    }

    public void setList(List<Wares> list) {
        this.list = list;
    }


}
