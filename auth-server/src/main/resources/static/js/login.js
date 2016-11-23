/**
 * Created by hushuming on 2016/11/20.
 */



$(document).ready(function () {
    var e = $('.blockError').text().trim();
    if (isEmpty(e)) {
        $("input").prop('disabled', false);
    } else {
        $("input").prop('disabled', true);
    }


    blur();


    $('#loginForm input').on('keyup blur', function () {
        //所有的输入框为非空，且错误提示为0
        if (allValidate()) {
            $('button.btn').prop('disabled', false);
        }
        else {
            $('button.btn').prop('disabled', 'disabled');
        }

    })
});


function blur() {
    $("#username").on("focus change keyup keydown blur", usernameValidate);
    $("#password").on("focus change keyup keydown blur", passwordValidate);
};

