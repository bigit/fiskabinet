$(document).ready(function () {
    let path = document.location.pathname;
    let $conextPath = path.substring(0, path.indexOf('/',1));

    let model_change = function () {
        $("form #modelId").val($(this).val());
    }

    $("#vendor_list").change(function () {
        let id = $("option:selected", this).val();
        $.get({
            url: $conextPath + "/models",
            data: {"id": id},
            success: function (data) {
                $("#model_list").replaceWith(data);
                $("#model_select").change(model_change);
            }
        });
    });
})