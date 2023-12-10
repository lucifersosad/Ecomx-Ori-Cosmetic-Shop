$(document).ready(function() {
	var originalTotalValue = $("#subTotalValue").html();

	$("#cod").on("change", function(e) {
		$("#codOption").css("display", "block");
		var codHtml = $("#codValue").html();
		var codValue = parseFloat(codHtml.replace(/[^\d.]/g, ''));

		var totalHtml = $("#totalValue").html();
		var totalValue = parseFloat(totalHtml.replace(/[^\d.]/g, ''));

		$("#totalValue").html((codValue + totalValue) + ".000 đ");
		$("#codOption").css("font-weight", "bold");
		$("#total").css("font-weight", "bold");
	});
	
	$(document).on("click", function(e) {
	    var clickedElement = $(e.target);
	    if (clickedElement.is("#vnpay") || clickedElement.is("#paypal")) {
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
	var fullName = document.querySelector('#fullName').value;
	var phone = document.querySelector('#phone').value;
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
			'&fullName=' + fullName +
			'&phone=' + phone +
			'&city=' + city +
			'&district=' + district +
			'&town=' + town +
			'&homeaddress=' + homeaddress,
	})
		.then(response => response.text())
		.then(data => {
			localStorage.setItem('updatedAddress', JSON.stringify({
			email: email,
			fullName: fullName,
			phone: phone,
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
	$("#fullName").prop("disabled", false);
	$("#phone").prop("disabled", false);
	$("#city").prop("disabled", false);
	$("#district").prop("disabled", false);
	$("#town").prop("disabled", false);
	$("#address").prop("disabled", false);
	$("#fullName").focus();
}

function loadUpdate() {
    // Kiểm tra nếu có thông tin đã lưu trong localStorage
    var updatedAddress = localStorage.getItem('updatedAddress');
    if (updatedAddress) {
        // Parse và áp dụng thông tin vào thẻ DOM
        var addressInfo = JSON.parse(updatedAddress);
        document.querySelector('#email').value = addressInfo.email;
        document.querySelector('#fullName').value = addressInfo.fullName;
        document.querySelector('#phone').value = addressInfo.phone;
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
        $('#email').val(addressInfo.email);
        $('#fullName').val(addressInfo.fullName);
        $('#phone').val(addressInfo.phone);
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