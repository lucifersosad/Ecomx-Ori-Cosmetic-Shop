$(document).ready(function() {
	var originalTotalValue = $("#totalValue").html();

	$("#cod").on("click", function(e) {
		$("#codOption").css("display", "block");
		var codHtml = $("#codValue").html();
		var codValue = parseFloat(codHtml.replace(/[^\d.]/g, ''));

		var totalHtml = $("#totalValue").html();
		var totalValue = parseFloat(totalHtml.replace(/[^\d.]/g, ''));

		$("#totalValue").html((codValue + totalValue) + ".000 VND");
		$("#codOption").css("font-weight", "bold");
		$("#total").css("font-weight", "bold");
	});

	$(document).on("mouseup", function(e) {
		var container = $("#cod");

		if (!container.is(e.target) && container.has(e.target).length === 0) {
			$("#totalValue").html(originalTotalValue);
			$("#codOption").css("display", "none");
			$("#total").css("font-weight", "normal");
		}
	});
});

function checkPaymentOption(form) {
    var paymentOption = $("input[name='optradio']:checked").val();
    console.log(paymentOption);
    if (paymentOption == "paypal") {
        var url = "/pay";
        window.location.href = url;
    }
    return false;
}