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

function updateQuantity(proid, qtt) {
	fetch('cart/updateQTT', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify({
			proid: proid,
			quantity: qtt
		}),
	})
		.then(response => response.text())
		.then(data => {
			console.log(data);
			location.reload();
		})
		.catch((error) => {
			console.error('Error:', error);
		});
}

document.addEventListener('click', function(event) {
	var target = event.target;
	if (target.classList.contains('btn-quantity')) {
		var row = target.closest('tr');
		
        var input = row.querySelector('.form-control');
        var inputValue = parseInt(input.value, 10);
        
        var proid = input.getAttribute('data-proid');

		var currentQuantity = parseInt(row.querySelector('.form-control').value, 10);
		console.log(currentQuantity);
		if (target.classList.contains('js-btn-minus')) {
			if (currentQuantity == 1) {
				window.location.href = "cart/deleteItem/" + proid;
			}

			if (currentQuantity > 1) {
				updateQuantity(proid, inputValue - 1);
			}
		} else if (target.classList.contains('js-btn-plus')) {

			updateQuantity(proid, inputValue + 1);
		}
	}
});