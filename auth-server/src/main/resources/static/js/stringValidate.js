/**
 * Created by hushuming on 2016/11/20.
 */
'use strict';
function isEmail(email) {

    var reg = /^\w[-.\w]*\@[-a-zA-Z0-9]+(\.[-a-zA-Z0-9]+)*\.(com|cn|net|edu|info|xyz|wang|org|top|ren|club|pub|rocks|band|market|software|social|lawyer|engineer|me|tv|cc|co|biz|mobi|name|asia)$/gi;
    return reg.test(email);

}
//验证手机号格式
function isPhone(phone) {

    var reg = /^(13[0-9]|15[012356789]|17[0135678]|18[0-9]|14[579])[0-9]{8}$/g;
    return reg.test(phone);
}
//password 格式验证
function isPassword(password) {

    var reg = /^(?=.*\d)(?=.*[A-Za-z]).{6,20}|(?=.*\d)(?=.*[-`=;',.~!@#$%^&*()_+\\{}:<>?]).{6,20}|(?=.*[A-Za-z])(?=.*[-`=;',.~!@#$%^&*()_+\\{}:<>?]).{6,20}|(?=.*\d)(?=.*[A-Za-z])(?=.*[-`=;',.~!@#$%^&*()_+\\{}:<>?]).{6,20}$/;
    return reg.test(password);
}
// 判断参数为空,传递进来的参数的样本： #username, #password
function isEmpty(input) {
    return (input == 0 || input == null);
}

//公司格式判断
function isCompanyName(company) {
    var reg = /^([\u4e00-\u9fa5\(\)（）]{2,20}|[a-zA-Z\.\s\(\)]{2,20})$/;
    return reg.test(company);
}
( jQuery );