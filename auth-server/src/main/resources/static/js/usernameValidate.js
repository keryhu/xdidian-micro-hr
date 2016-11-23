/**
 * Created by hushuming on 2016/11/20.
 */


'use strict';

function usernameValidate() {
    var username = $.trim($("#username").val());
    var usernameEmpty = isEmpty(username);
    //既不是email也不是phone
    var neitherEmailNorPhone = !isEmail(username) && !isPhone(username);

    if (usernameEmpty) {
        $("#usernameControlGroup").find('.help-inline').html('请输入邮箱/手机');
    }
    //如果account既不是email格式也不是phone格式
    else if (neitherEmailNorPhone && !usernameEmpty) {
        $("#usernameControlGroup").find('.help-inline').html('邮箱/手机格式不对');
    }
    else {
        $.get("query/validateLoginName",
            {loginName: username},
            function (response) {
                //验证前先清空错误
                $("#usernameControlGroup").removeClass('error');
                $("#usernameControlGroup").find('.help-inline').html('');
                if (!isEmpty(response.error)) {
                    $("#usernameControlGroup").find('.help-inline').html(response.error);
                }
                else {
                    $("#usernameControlGroup").find('.help-inline').html('');
                }

            })
    }

}