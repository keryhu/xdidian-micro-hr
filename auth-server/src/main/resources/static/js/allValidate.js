/**
 * Created by hushuming on 2016/11/20.
 */


'use strict';

function allValidate() {
    var username = $.trim($("#username").val());
    var usernameEmpty = isEmpty(username);
    var password = $.trim($("#password").val());
    var passwordEmpty = isEmpty(password);
    var noEmpty = (!usernameEmpty && !passwordEmpty);
    var errorNum = 0;
    //统计页面错误显示的个数。
    $(".control-group").find('.help-inline')
        .each(function () {
                var e = $(this).text().trim();
                if (!isEmpty(e))
                    errorNum++;
            }
        )

    var m=$.trim($(".loginError").val());

    return noEmpty && errorNum === 0;
}