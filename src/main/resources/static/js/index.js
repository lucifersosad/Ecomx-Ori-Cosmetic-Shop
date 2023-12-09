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

function updateAddress() {
	var email = document.querySelector('#email').value;
	var city = document.querySelector('#city').value;
	var district = document.querySelector('#district').value;
	var town = document.querySelector('#town').value;
	var homeaddress = document.querySelector('#address').value;

	// Gửi request tới controller để cập nhật địa chỉ
	fetch('/web/users/updateAddress', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
		},
		body: 'email=' + email +
			'&city=' + city +
			'&district=' + district +
			'&town=' + town +
			'&homeaddress=' + homeaddress,
	})
		.then(response => response.text())
		.then(data => {
			localStorage.setItem('updatedAddress', JSON.stringify({
            city: city,
            district: district,
            town: town,
            homeaddress: homeaddress,
            data: data,
        }));
			location.reload();
		})
		.catch(error => console.error('Error:', error));
}
function updateUser() {
	$(".btn-update").css("display", "block");
	$("#city").prop("disabled", false);
	$("#district").prop("disabled", false);
	$("#town").prop("disabled", false);
	$("#address").prop("disabled", false);
}

function loadUpdate() {
    // Kiểm tra nếu có thông tin đã lưu trong localStorage
    var updatedAddress = localStorage.getItem('updatedAddress');
    if (updatedAddress) {
        // Parse và áp dụng thông tin vào thẻ DOM
        var addressInfo = JSON.parse(updatedAddress);
        document.querySelector('#city').value = addressInfo.city;
        document.querySelector('#district').value = addressInfo.district;
        document.querySelector('#town').value = addressInfo.town;
        document.querySelector('#address').value = addressInfo.homeaddress;
        document.querySelector('#thong-bao').innerHTML = "cc";
        console.log("OK");

        // Xóa thông tin đã lưu trong localStorage
        localStorage.removeItem('updatedAddress');
    }
}

$(document).ready(function() {
    // Kiểm tra nếu có thông tin đã lưu trong localStorage
    var updatedAddress = localStorage.getItem('updatedAddress');
    if (updatedAddress) {
        // Parse và áp dụng thông tin vào thẻ DOM
        var addressInfo = JSON.parse(updatedAddress);
        $('#city').val(addressInfo.city);
        $('#district').val(addressInfo.district);
        $('#town').val(addressInfo.town);
        $('#address').val(addressInfo.homeaddress);
        $('#thong-bao').html(addressInfo.data);
        console.log("OK");
        // Xóa thông tin đã lưu trong localStorage
        localStorage.removeItem('updatedAddress');
    }
});