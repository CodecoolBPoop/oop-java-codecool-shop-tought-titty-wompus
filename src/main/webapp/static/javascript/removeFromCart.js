function setEventListeners() {
    $('.remove-item').click(removeItem, event);
}

function removeItem(event) {
    let id = event.target.dataset["productid"];
    $.post("/remove-from-cart", {id: id});
    event.target.closest('tr').remove();
    let subTotal = parseFloat(event.target.dataset["subtotal"]);
    let total = document.getElementById("total");
    total.innerHTML = String(parseFloat(total.innerText) - subTotal);

}

setEventListeners();