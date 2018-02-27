$(document).ready(function () {
    $("#txtNewPassword, #txtConfirmPassword").keyup(checkPasswordMatch);
});

function checkPasswordMatch() {
    let password = $("#txtNewPassword").val();
    let confirmPassword = $("#txtConfirmPassword").val();
    if (password === "" && confirmPassword === "") {
        $("#checkPasswordMatch").html("");
        $("#updateProfileButton").prop('disabled', false);
    } else {
        if (password !== confirmPassword) {
            $("#checkPasswordMatch").html("Passwords do not match!").css("color", "red");
            $("#updateProfileButton").prop('disabled', true);
        } else {
            $("#checkPasswordMatch").html("Passwords match!").css("color", "green");
            $("#updateProfileButton").prop('disabled', false);
        }
    }
}

function calculateChangeAmount(defaultAmount) {
    if (defaultAmount <= 10) {
        return 1;
    } else if (defaultAmount <= 50) {
        return 5;
    } else if (defaultAmount <= 150) {
        return 10;
    } else {
        return 20;
    }
}


$("input[id*='input']").on("change, paste, keyup", function () {
    changeButton(this.id);
});

function changeButton(inputid) {
    let inputId = inputid;
    let amount = $("#" + inputId).val();
    if (amount < 0) {
        amount = Math.abs(amount);
        $("#" + inputId).val(amount);
    }
    const id = inputId.replace("input", "");
    const addButtonId = "#addButton" + id;
    const removeButtonId = "#removeButton" + id;
    $(addButtonId).html("<i class=\"fas fa-angle-double-up\"></i> <span>" + amount + "</span>")
    $(removeButtonId).html("<i class=\"fas fa-angle-double-down\"></i> <span>" + amount + "</span>")
}


function addInputAmount(id, defaultAmount) {
    const inputId = "#input" + id;
    const addButtonId = "#addButton" + id;
    const removeButtonId = "#removeButton" + id;
    let inputAmount;
    let changeAmount = calculateChangeAmount(defaultAmount);
    if (!$(inputId).val()) {
        inputAmount = changeAmount
    } else {
        inputAmount = parseInt($(inputId).val(), 10);
        if (inputAmount < 0) {
            inputAmount = 0;
        } else {
            inputAmount += changeAmount;
        }
    }
    $(inputId).val(inputAmount);
    $(addButtonId).html("<i class=\"fas fa-angle-double-up\"></i> <span>" + inputAmount.toString() + "</span>")
    $(removeButtonId).html("<i class=\"fas fa-angle-double-down\"></i> <span>" + inputAmount.toString() + "</span>")
}

function removeInputAmount(id, defaultAmount) {
    const inputId = "#input" + id;
    const addButtonId = "#addButton" + id;
    const removeButtonId = "#removeButton" + id;
    let inputAmount;
    let changeAmount = calculateChangeAmount(defaultAmount);
    if (!$(inputId).val()) {
        inputAmount = 0
    } else {
        inputAmount = parseInt($(inputId).val(), 10);
        if (inputAmount <= 0) {
            inputAmount = 0;
        } else {
            inputAmount -= changeAmount;
        }
    }
    $(inputId).val(inputAmount);
    $(addButtonId).html("<i class=\"fas fa-angle-double-up\"></i> <span>" + inputAmount.toString() + "</span>")
    $(removeButtonId).html("<i class=\"fas fa-angle-double-down\"></i> <span>" + inputAmount.toString() + "</span>")
}

function addNewCategoryForm() {

    $("#addNewCategoryButton").hide();
    $("#deleteCategoryButton").hide();
    $("#addNewCategoryForm").show();
    $("#deleteCategoryForm").hide();
}

function deleteCategoryForm() {

    $("#addNewCategoryButton").hide();
    $("#deleteCategoryButton").hide();
    $("#addNewCategoryForm").hide();
    $("#deleteCategoryForm").show();
}

function cancelCategoryForms() {

    $("#addNewCategoryButton").show();
    $("#deleteCategoryButton").show();
    $("#addNewCategoryForm").hide();
    $("#deleteCategoryForm").hide();
}

function addAmount(productId) {
    const inputId = "#input" + productId;
    const amount = $(inputId).val();
    sendAjaxChange(productId, amount)
}

function removeAmount(productId) {
    const inputId = "#input" + productId;
    const amount = -$(inputId).val();
    sendAjaxChange(productId, amount)
}

function sendAjaxChange(productId, amount) {

    if (amount === "input") {
        const inputId = "#input" + productId;
        if (!$(inputId).val()) {
            amount = 0
        } else {
            amount = Math.abs(parseInt($(inputId).val(), 10));
        }
    } else if (amount === "-input") {
        const inputId = "#input" + productId;
        if (!$(inputId).val()) {
            amount = 0
        } else {
            amount = -Math.abs(parseInt($(inputId).val(), 10));
        }
    }

    if (amount !== 0) {
        let change = {};

        change.productId = productId;
        change.changeAmount = amount;

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/ajax/newChange",
            data: JSON.stringify(change),
            dataType: "json",
            cache: false,
            timeout: 3000,
            success: function (data) {

                let div_inventory = "";
                let i = 0;
                $.each(data.result, function () {
                    if (data.result[i].products.length !== 0) {
                        let div_category = "";
                        let j = 0;
                        div_category += "<h4>" + data.result[i].name + "</h4>";
                        div_category += "<table border=\"0\" class=\"table table-hover table-sm\">\n" +
                            "<thead>\n" +
                            "<tr>\n" +
                            "<th class=\"w-35\">Name</th>\n" +
                            "<th class=\"w-15\">Amount</th>\n" +
                            "<th class=\"w-50\" style=\"text-align: center\">Actions</th>\n" +
                            "<tr>\n" +
                            "</thead>\n" +
                            "<tbody>\n";
                        $.each(this.products, function () {
                            let product = data.result[i].products[j];
                            div_category += "<tr>";
                            div_category += "<td class=\"align-middle\" onclick=\"productClicked(\'" + product.id + "\', \'0\');\">" + product.name + "</td>";
                            div_category += "<td class=\"align-middle\" onclick=\"productClicked(\'" + product.id + "\', \'0\');\">" + product.amount + "</td>"
                            div_category += "<td>\n" +
                                "<div class=\"btn-toolbar w-100 justify-content-center\">\n" +
                                "<button class=\"btn btn-primary m-1\" onclick=\"addAmount(\'" + product.id + "\');\" id=\"addButton" + product.id + "\"><i\n" +
                                "class=\"fas fa-angle-double-up\"></i> <span>" + product.defaultChange + "</span></button>\n" +
                                "<div class=\"input-group m-1\">\n" +
                                "<div class=\"input-group-prepend\">\n" +
                                "<button class=\"btn btn-outline-secondary\" type=\"button\" onclick=\"removeInputAmount(\'" + product.id + "\',\'" +
                                product.defaultChange + "\');\"><i class=\"fas fa-angle-left\"></i></button>\n" +
                                "</div>\n" +
                                "<input type=\"number\" step=\"1\" class=\"form-control d-none d-sm-block\" id=\"input" + product.id + "\" value=\"" + product.defaultChange + "\">\n" +
                                "<div class=\"input-group-append\">\n" +
                                "<button class=\"btn btn-outline-secondary\" type=\"button\" onclick=\"addInputAmount(\'" + product.id + "\',\'" +
                                product.defaultChange + "\');\"><i class=\"fas fa-angle-right\"></i></button>\n" +
                                "</div>\n" +
                                "</div>\n" +
                                "<button class=\"btn btn-primary m-1\" onclick=\"removeAmount(\'" + product.id + "\');\"" +
                                " id=\"removeButton" + product.id + "\"><i\n" +
                                "class=\"fas fa-angle-double-down\"></i> <span>" + product.defaultChange + "</span></button>\n" +
                                "</div>\n" +
                                "</td>";
                            div_category += "</tr>";
                            j++;
                        });
                        div_category += "</tbody>\n" +
                            "</table>";
                        div_inventory += div_category;
                    }
                    i++;
                });
                $("#inventory").html(div_inventory);
                $("#btnUndo").prop('disabled', false);
                $("input[id*='input']").on("change, paste, keyup", function () {
                    changeButton(this.id);
                });
            },
            error: function (textStatus) {
                console.log(textStatus);
            }
        });
    }
}

function productClicked(id, fullChangeList) {
    location.href = "/home/product?id=" + id + "&fullChangeList=" + fullChangeList;
}

function editComment(id) {
    const commentBody = "#commentBody" + id;
    const commentReply = "#commentReply" + id;
    const commentEdit = "#commentEdit" + id;
    const commentDelete = "#commentDelete" + id;
    $(commentBody).hide()
    $(commentReply).hide();
    $(commentEdit).show();
    $(commentDelete).hide();
}

function cancelEditComment(id) {
    const commentBody = "#commentBody" + id;
    const commentEdit = "#commentEdit" + id;
    $(commentBody).show();
    $(commentEdit).hide();
}

function replyComment(id) {
    const commentReply = "#commentReply" + id;
    const commentEdit = "#commentEdit" + id;
    const commentDelete = "#commentDelete" + id;
    $(commentReply).show();
    $(commentEdit).hide();
    $(commentDelete).hide();
}

function cancelReplyComment(id) {
    const commentReply = "#commentReply" + id;
    $(commentReply).hide();
}

function deleteComment(id) {
    const commentReply = "#commentReply" + id;
    const commentEdit = "#commentEdit" + id;
    const commentDelete = "#commentDelete" + id;
    $(commentReply).hide();
    $(commentEdit).hide();
    $(commentDelete).show();
}

function cancelDeleteComment(id) {
    const commentDelete = "#commentDelete" + id;
    $(commentDelete).hide();
}

function leaveRequestForm() {
    $("#leaveRequestButton").hide();
    $("#leaveRequestForm").show();
}