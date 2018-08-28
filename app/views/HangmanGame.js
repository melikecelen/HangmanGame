function buttonCreator(dataList, className, containerName) {
    for (let i in dataList) {
        let newButton = document.createElement('button');
        newButton.value = JSON.stringify(dataList[i]);
        if (dataList[i].letter !== undefined)
            newButton.innerText = dataList[i].letter + ":" + dataList[i].cost;
        else {
            newButton.innerText = dataList[i];
        }
        newButton.className += className;
        document.getElementById(containerName).appendChild(newButton);
    }
    let buttons = document.getElementsByClassName(className);
    for (let i = 0; i < buttons.length; i++) {
        buttons[i].addEventListener("click", pickLetterFunc, false);
    }
}

function cardCreator(cardList) {
    for (let i in cardList) {
        let newInput = document.createElement('button');
        newInput.className += 'card-button';
        newInput.value = JSON.stringify(cardList[i]);
        newInput.innerText = JSON.stringify(cardList[i].name);
        newInput.name = JSON.stringify(cardList[i]);
        newInput.object = cardList[i];

        newInput.classList.add('usable');

        document.getElementById('cards').appendChild(newInput);
    }
    let buttons = document.getElementsByClassName('card-button');
    for (let i = 0; i < buttons.length; i++) {
        buttons[i].addEventListener("click", pickCardFunc, false);
    }

}

function pickCardFunc(event) {
    document.getElementById('active-card').textContent = event.target.innerText;
    document.getElementById('active-card').value=event.target.innerText;
    var obj1 = {cardName: document.getElementById('active-card').textContent.replace(/['"]+/g, '')};
    switch (event.target.innerText.replace(/['"]+/g, '')) {
        case "Category":
            get('http://localhost:9000/guess', 'POST', JSON.stringify(obj1)).then(function (data) {
                document.getElementById('secretword').textContent = data.response.message.secretWord;
                document.getElementById('point').textContent = data.response.message.point;
                document.getElementById('category').textContent = data.response.message.catName;
                document.getElementById('active-card').textContent='';

            });
            event.target.disabled = "disabled";
            event.target.classList.remove('usable');
            break;
    }
    if (event.target.innerText.replace(/['"]+/g, '') !== "Category" && event.target.value.replace(/['"]+/g, '') !== '') {
        let cards = document.getElementsByClassName('usable');
        for (c in cards) {
            cards[c].disabled = "disabled";
        }
    }

}

function pickLetterFunc(event) {
    if (document.getElementById('active-card').textContent !== '') {
        event.target.value['cardName'] = document.getElementById('active-card').textContent;
    }
    let obj1 = {};
    if (document.getElementById('active-card').textContent !== '') {
        obj1 = {cardName: document.getElementById('active-card').textContent.replace(/['"]+/g, '')};
    }
    let obj2 = JSON.parse(event.target.value);
    let obj3 = Object.assign(obj1, obj2);

    get('http://localhost:9000/guess', 'POST', JSON.stringify(obj3)).then(function (data) {
        if (data.response.message.result === false)
            event.target.className += " usedLetterFalse";
        else event.target.className += " usedLetterTrue";
        document.getElementById('secretword').textContent = data.response.message.secretWord;
        document.getElementById('point').textContent = data.response.message.point;

        document.getElementById('active-card').textContent = '';
        event.target.disabled = "disabled";

        let cards = document.getElementsByClassName('usable');
        for (c in cards) {
            cards[c].disabled = "";
        }
    });

}

function get(url, method, data) {
    'use strict';
    let request = new XMLHttpRequest();
    request.responseType = 'json';
    return new Promise(function (resolve, reject) {
        request.onreadystatechange = function () {
            if (request.readyState !== 4) return;
            if (request.status >= 200 && request.status < 300) {
                resolve(request);
            }
            else {
                reject({
                    status: request.status,
                    statusText: request.statusText
                });
            }
        };
        request.open(method, url);
        request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        request.send(data);

    });
}

get('http://localhost:9000/createANewGame', 'POST', '{"level":2}').then(function (data) {
    document.getElementById('secretword').textContent = data.response.secretWord;
    document.getElementById('point').textContent += data.response.point;
    buttonCreator(data.response.alphabet, 'letter-button', 'letter-container');
    cardCreator(data.response.cardList);
    moveList=data.response.moveList;
});