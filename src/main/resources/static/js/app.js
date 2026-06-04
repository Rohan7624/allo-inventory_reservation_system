window.onload = function () {

    if (document.getElementById("inventory-container")) {
        loadInventory();
    }

    if (document.getElementById("reservation-container")) {
        loadReservation();
    }
};

async function loadInventory() {

    try {

        const response = await fetch("/api/inventory");

        if (!response.ok) {
            throw new Error("Failed to load inventory");
        }

        const inventory = await response.json();

        const container = document.getElementById("inventory-container");

        container.innerHTML = "";

        inventory.forEach(item => {

            container.innerHTML += `
                <div class="card">

                    <h3>${item.productName}</h3>

                    <p><strong>Inventory ID:</strong> ${item.inventoryId}</p>

                    <p><strong>Warehouse:</strong> ${item.warehouseName}</p>

                    <p><strong>Total Stock:</strong> ${item.totalStock}</p>

                    <p><strong>Reserved Stock:</strong> ${item.reservedStock}</p>

                    <p><strong>Available Stock:</strong> ${item.availableStock}</p>

                    <p>
                        <strong>Quantity:</strong>
                        <input
                            type="number"
                            id="qty-${item.inventoryId}"
                            min="1"
                            max="${item.availableStock}"
                            value="1">
                    </p>

                    <button onclick="reserve(${item.inventoryId})">
                        Reserve
                    </button>

                </div>
            `;
        });

    } catch (error) {

        console.error(error);

        document.getElementById("inventory-container").innerHTML =
            "<p>Unable to load inventory.</p>";
    }
}

async function reserve(inventoryId) {

    try {

        const quantity = parseInt(
            document.getElementById(`qty-${inventoryId}`).value
        );

        const response = await fetch("/api/reservations", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                inventoryId: inventoryId,
                quantity: quantity
            })
        });

        if (response.status === 409) {
            alert("Not enough stock available.");
            return;
        }

        if (!response.ok) {

            const errorText = await response.text();

            alert("Error: " + errorText);

            return;
        }

        const reservation = await response.json();

        localStorage.setItem(
            "reservation",
            JSON.stringify(reservation)
        );

        window.location.href = "/reservation";

    } catch (error) {

        console.error(error);

        alert("Failed to create reservation.");
    }
}

function loadReservation() {

    const reservation =
        JSON.parse(localStorage.getItem("reservation"));

    if (!reservation) {

        document.getElementById("reservation-container").innerHTML =
            "<p>No reservation found.</p>";

        return;
    }

    document.getElementById("reservation-container").innerHTML = `
        <p><strong>Reservation Id:</strong> ${reservation.reservationId}</p>
        <p><strong>Status:</strong> ${reservation.status}</p>
        <p><strong>Quantity:</strong> ${reservation.quantity}</p>
        <p><strong>Expires At:</strong> ${reservation.expiresAt}</p>
    `;

    document.getElementById("confirmBtn").onclick =
        () => confirmReservation(reservation.reservationId);

    document.getElementById("cancelBtn").onclick =
        () => releaseReservation(reservation.reservationId);

    startCountdown(reservation.expiresAt);
}

//function startCountdown(expiresAt) {
//
//    const countdown = document.createElement("h3");
//
//    countdown.id = "countdown";
//
//    document.getElementById("reservation-container")
//        .appendChild(countdown);
//
//    console.log("=================================");
//    console.log("Countdown Started");
//    console.log("expiresAt String:", expiresAt);
//    console.log("Parsed Date:", new Date(expiresAt));
//    console.log("=================================");
//
//    const timer = setInterval(() => {
//
//        const now = new Date().getTime();
//        const expiry = new Date(expiresAt).getTime();
//        const difference = expiry - now;
//
//        console.log("Now:", new Date(now));
//        console.log("Expiry:", new Date(expiry));
//        console.log("Now Millis:", now);
//        console.log("Expiry Millis:", expiry);
//        console.log("Difference:", difference);
//
//        if (difference <= 0) {
//
//            console.log("Reservation Expired");
//
//            clearInterval(timer);
//
//            countdown.innerHTML = "Reservation Expired";
//
//            return;
//        }
//
//        const minutes = Math.floor(difference / (1000 * 60));
//        const seconds = Math.floor((difference % (1000 * 60)) / 1000);
//
//        console.log(`Time Left: ${minutes}m ${seconds}s`);
//
//        countdown.innerHTML =
//            `Time Left: ${minutes}m ${seconds}s`;
//
//    }, 1000);
//}
function startCountdown(expiresAt) {

       const countdown = document.createElement("h3");
       countdown.id = "countdown";

       document.getElementById("reservation-container")
           .appendChild(countdown);

       // Add IST offset manually
       const expiry = new Date(expiresAt).getTime() + (5.5 * 60 * 60 * 1000);

       console.log("Original:", expiresAt);
       console.log("Adjusted Expiry:", new Date(expiry));

       const timer = setInterval(() => {

           const now = Date.now();
           const difference = expiry - now;

           if (difference <= 0) {
               clearInterval(timer);
               countdown.innerHTML = "Reservation Expired";
               return;
           }

           const minutes = Math.floor(difference / (1000 * 60));
           const seconds = Math.floor((difference % (1000 * 60)) / 1000);

           countdown.innerHTML = `Time Left: ${minutes}m ${seconds}s`;

       }, 1000);
   }

async function confirmReservation(reservationId) {

    try {

        const response = await fetch(
            `/api/reservations/${reservationId}/confirm`,
            {
                method: "POST"
            }
        );

        if (response.status === 410) {

            alert("Reservation expired.");

            return;
        }

        if (!response.ok) {

            alert("Failed to confirm reservation.");

            return;
        }

        alert("Purchase confirmed.");
        window.location.href = "/";

    } catch (error) {

        console.error(error);

        alert("Server error occurred.");
    }
}

async function releaseReservation(reservationId) {

    try {

        const response = await fetch(
            `/api/reservations/${reservationId}/release`,
            {
                method: "POST"
            }
        );

        if (!response.ok) {

            alert("Failed to release reservation.");

            return;
        }

        localStorage.removeItem("reservation");

        alert("Reservation released.");

        window.location.href = "/";

    } catch (error) {

        console.error(error);

        alert("Server error occurred.");
    }
}