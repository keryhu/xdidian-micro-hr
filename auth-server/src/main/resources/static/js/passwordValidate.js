/**
 * Created by hushuming on 2016/11/20.
 */

'use strict';

function passwordValidate(){
    var password = $.trim($("#password").val());
    var passwordEmpty=isEmpty(password);

    var passwordPattern=isPassword(password);
    var passwordNotInSize=(password.length < 6|| password.length > 20);
    var message='';

    if (passwordEmpty){
        $("#passwordControlGroup").find('.help-inline').html("请输入密码");
    }
    else if (!passwordEmpty && !passwordPattern) {
        if (!passwordNotInSize) {
            // 如果密码非空，在6-20字符之间，但是不是2种组合，报这个错误
            message='至少包含2种组合';
        } else {
            message='必须6-20位';
        }
        $("#passwordControlGroup").find('.help-inline').html(message);
    }

    else if(passwordPattern){
        console.log('geshi')
        $("#passwordControlGroup").find('.help-inline').html('');
    }

}