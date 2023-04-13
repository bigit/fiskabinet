$(document).ready(function () {

    $("form").submit(function (event) {
        // event.preventDefault();
        let phone_inp = $("form [name=phone]");
        let phone = phone_inp.val().replaceAll(/\D/g,"");
        phone_inp.val(phone);
        return true;
    })
})