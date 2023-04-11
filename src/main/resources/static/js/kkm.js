$(document).ready(function () {

    $("#vendor_list").change(function () {
        let id = $("option:selected", this).val();
        $.get({
            url: "/models",
            data: { "id" : id },
            success: function (data) {
                $("#model_list").replaceWith(data);
            }
        });
    });
})