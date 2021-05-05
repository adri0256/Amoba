let interval = null;

$(document).ready(function (){
    $('#back').click(function (e) {
        back();
    });

    $('#forward').click(function (e) {
        forward();
    });

    $('#autoPlay').click(function (e) {
        interval = setInterval(autoPlay, 1000);
    });

    $('#stopAutoPlay').click(function (e) {
       clearInterval(interval);
    });
});

function forward(e){
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "../GameController",
        data: {
            id: "forward"
        },
        success: function (result){
            success("forward", result);
        },
        error: function (result){
            alert("Failed");
        }
    });
}

function back(e){
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "../GameController",
        data: {
            id: "back"
        },
        success: function (result){
            success("back", result);
        },
        error: function (result){
            alert("Failed");
        }
    });
}

function autoPlay(){
    console.log("yes");
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "../GameController",
        data: {
            id: "forward"
        },
        success: function (result){
            success("forward", result);
        },
        error: function (result){
            alert("Failed");
        }
    });
}

function success(direction, result) {
    const IMG_X = "<img class='symbol' src='https://youssefragab.com/themes/thunder-en/wp-content/themes/Thunder/images/x.png'  alt='X'/>";
    const IMG_O = "<img class='symbol' src='https://pngimg.com/uploads/letter_o/letter_o_PNG118.png' alt='O' />";
    const winnerSpan = $('#winner');
    const currentPlayerSpan = $('#currentPlayer');

    console.log(result);

    let cell = "#cell_" + result.coords;

    if(direction === "forward") {
        if(parseInt(result.symbol) === 1) {
            $(cell).html(IMG_X);
        } else {
            $(cell).html(IMG_O);
        }

        if(result.player !== undefined)
            currentPlayerSpan.html("Current Player: " + result.player);

        if(result.end) {
            winnerSpan.html("Winner: " + result.player);
            clearInterval(interval);
        }
    }
    if(direction === "back") {
        $(cell).html("");

        if(result.begin){
            currentPlayerSpan.html("");
        } else {
            currentPlayerSpan.html("Current Player: " + result.player);
        }

        winnerSpan.html("");
    }

}