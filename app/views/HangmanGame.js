let moveList;
let cardList;
let Point = 100;
let word;

function isCardAffordable(card) {
    return Point >= card.minPoint && Point <= card.maxPoint;
}
function updateCardsUsability(cardList) {
    let buttons = document.getElementsByClassName('card-button');
    for (let i = 0; i < buttons.length; i++) {
        if (cardList[i].availableCount > 0 && isCardAffordable(cardList[i])) {
            buttons[i].disabled = "";
        }
        else buttons[i].disabled = "disabled";
    }
    if (moveList !== null || moveList !== undefined) {
        if (moveList.length !== 0)
            if (moveList[moveList.length - 1].card === "Consolation" && moveList[moveList.length - 1].result === false || moveList[moveList.length - 1].card === "Risk" && moveList[moveList.length - 1].result === true) {
                for (let i = 0; i < buttons.length; i++) {
                    buttons[i].disabled = "disabled";
                }
                document.getElementById('active-card').textContent = "\""+moveList[moveList.length - 1].card+"\"";
            }
    }
}

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

function cardHeadersCreator(newInput, inputData, id) {
    let newHeader = document.createElement('h3');
    newHeader.textContent = inputData;
    newHeader.id = id;
    newInput.appendChild(newHeader);
}

function cardCreator(cardList) {
    for (let i in cardList) {
        let newInput = document.createElement('button');
        newInput.className += 'card-button';
        newInput.id = cardList[i].name;
        newInput.value = JSON.stringify(cardList[i]);
        newInput.name = JSON.stringify(cardList[i].name);
        newInput.object = cardList[i];
        newInput.classList.add('usable');
        let explanationButton = document.createElement('button');
        explanationButton.className = "tooltip";
        explanationButton.innerText = "?";
        explanationButton.style.zIndex = 1;
        let explanationText = document.createElement('span');
        explanationText.className = "tooltip-text";
        explanationText.textContent = cardExplanation[i];
        explanationText.zIndex = 2;
        explanationButton.appendChild(explanationText);
        document.getElementById('cards').appendChild(explanationButton);

        cardHeadersCreator(newInput, cardList[i].name, cardList[i].name + "name");
        cardHeadersCreator(newInput, "Cost:" + cardList[i].cost, cardList[i].name + "cost");
        cardHeadersCreator(newInput, "Available:" + cardList[i].availableCount, cardList[i].name + "Available");

        document.getElementById('cards').appendChild(newInput);
    }
    let buttons = document.getElementsByClassName('card-button');
    for (let i = 0; i < buttons.length; i++) {
        buttons[i].addEventListener("click", pickCardFunc, false);
    }
    updateCardsUsability(cardList);
}

function pickCardFunc(event) {
    document.getElementById('active-card').textContent = event.target.name;
    document.getElementById('active-card').value = event.target.name;
    var obj1 = {cardName: document.getElementById('active-card').value.replace(/['"]+/g, '')};
    switch (event.target.name.replace(/['"]+/g, '')) {
        case "Category":
            get('http://localhost:9000/guess', 'POST', JSON.stringify(obj1)).then(function (data) {
                document.getElementById('point').textContent = data.response.message.point;
                document.getElementById('category').textContent += data.response.message.catName;
                document.getElementById('active-card').textContent = '';
                document.getElementById('active-card').value = ''; /////new

            });
            break;
        case "Buy A Letter":
            let letterButtons = document.getElementsByClassName('letter-button');
            for (let i = 0; i < letterButtons.length; i++)
                letterButtons[i].disabled = 'disabled';
            let spanContainer = document.getElementById('secret-letters').getElementsByTagName('span');
            for (let i = 0; i < spanContainer.length; i++) {
                if (spanContainer[i].innerText !== "*") {
                    console.log(spanContainer[i]);
                    spanContainer[i].disabled = 'disabled';
                    spanContainer[i].style.cursor = 'not-allowed';
                }
            }
            break;
    }
    cardList.forEach(function (c) {
        if (c.name === JSON.parse(event.target.value).name) {
            c.availableCount -= 1;
            document.getElementById(c.name + "Available").textContent = "Available:" + c.availableCount;
        }
    });
    updateCardsUsability(cardList);
    if (event.target.name.replace(/['"]+/g, '') !== "Category" && event.target.name.replace(/['"]+/g, '') !== '') {
        let cards = document.getElementsByClassName('card-button');
        for (let c in cards) {
            cards[c].disabled = "disabled";
        }
    }
}
function pickLetterFunc(event) {
    if (document.getElementById('active-card').value !== '') {
        event.target.value['cardName'] = document.getElementById('active-card').value;
    }
    let obj1 = {};
    if (document.getElementById('active-card').value !== '') {
        console.log(document.getElementById('active-card').value);
        obj1 = {cardName: document.getElementById('active-card').value.replace(/['"]+/g, '')};
    }
    let obj2 = JSON.parse(event.target.value);
    let obj3 = Object.assign(obj1, obj2);

    get('http://localhost:9000/guess', 'POST', JSON.stringify(obj3)).then(function (data) {
        if (data.response.message.result === false)
            event.target.className += " usedLetterFalse";
        else event.target.className += " usedLetterTrue";
        event.target.disabled = 'disabled';

        document.getElementById('point').textContent = data.response.message.point;
        Point = data.response.message.point;
        moveList = data.response.moves;
        updateCardsUsability(cardList);

        if (moveList[moveList.length - 1].card === "Consolation" && moveList[moveList.length - 1].result === false || moveList[moveList.length - 1].card === "Risk" && moveList[moveList.length - 1].result === true) {
            document.getElementById('active-card').value = '';
        }
        else {
            document.getElementById('active-card').textContent = '';
            document.getElementById('active-card').value = '';
        }

        if (data.response.message.gameState.finished === true) {
            gameFinsihed(data.response.message.gameState.message);
        }

        let spanContainer = document.getElementById('secret-letters').getElementsByTagName('span');
        let secretWord = data.response.message.secretWord;

        for (let i = 0; i < spanContainer.length; i++) {
            spanContainer[i].textContent = secretWord[i];
        }
    });
    updateCardsUsability(cardList);
}

function gameFinsihed(message) {
    alert(message + "! Secret Word: " + word);
    window.location.reload(false);
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
function buyALetter(event) {
    let obj1 = {cardName: document.getElementById('active-card').value.replace(/['"]+/g, '')};
    if (document.getElementById('active-card').value.replace(/['"]+/g, '') === "Buy A Letter") {

        let obj2 = {position: JSON.parse(event.target.id)};
        let obj3 = Object.assign(obj1, obj2);
        get('http://localhost:9000/guess', 'POST', JSON.stringify(obj3)).then(function (data) {
            let spanContainer = document.getElementById('secret-letters').getElementsByTagName('span');
            let secretWord = data.response.message.secretWord;
            for (let i = 0; i < spanContainer.length; i++) {
                spanContainer[i].textContent = secretWord[i];
            }
            document.getElementById('point').textContent = data.response.message.point;
            document.getElementById('active-card').textContent = '';
            document.getElementById('active-card').value = '';
            updateCardsUsability(cardList);
        });
        let letterButtons = document.getElementsByClassName('letter-button');
        if (event.target.innerText === "*")
            for (let i = 0; i < letterButtons.length; i++)
                if (!(letterButtons[i].classList.contains('usedLetterFalse') || letterButtons[i].classList.contains('usedLetterTrue')))
                    letterButtons[i].disabled = '';
    }
}

function levelButton() {
    var levelButtons = document.getElementsByClassName('level');
    for (let i = 0; i < levelButtons.length; i++) {
        levelButtons[i].addEventListener('click', chooseALevel, false);
    }
    document.getElementById('level-container').removeAttribute('hidden');
}

document.getElementById('home').addEventListener('click', refresh, false);

function chooseALevel(event) {
    let level = {"level": JSON.parse(event.target.id)};
    get('http://localhost:9000/createANewGame', 'POST', JSON.stringify(level)).then(function (data) {
        document.getElementById('point').textContent += data.response.point;
        moveList = data.response.moveList;
        cardList = data.response.cardList;
        Point = data.response.point;
        word = data.response.word;
        buttonCreator(data.response.alphabet, 'letter-button', 'letter-container');
        cardCreator(data.response.cardList);

        for (let i = 0; i < data.response.secretWord.length; i++) {
            var newSpanElement = document.createElement('span');
            newSpanElement.id = i;
            if (data.response.secretWord[i] === " ") {
                newSpanElement.innerText = " - ";
                newSpanElement.style.cursor = "not-allowed";
            }
            else {
                newSpanElement.innerText = "*";
                newSpanElement.addEventListener("click", buyALetter, false);
            }
            document.getElementById('secret-letters').appendChild(newSpanElement);
        }

        document.getElementById('active-card').value = '';
        document.getElementById('active-card').textContent = '';

    });
    show(document.getElementById('container'), document.getElementById('level-container'));
}

let cardExplanation = ["Usage: If you have 20 or more points. you can use only 1 time",
    "Usage: Choose a letter. If the guess is incorrect, the cost of next guess will be decreased by 50% and rounded down.",
    "Usage: You can learn the category of the word",
    "Usage: Make a letter guess. Cost of the letter is decreased 75% and rounded down. This card can be only used when remaining points are less than 40.",
    "Usage: Make a letter guess. If the guess is correct, the next guess will be free. This card can be only used when your point is less than 50 and greater than 25."];
levelButton();

function show(shown, hidden) {
    hidden.style.display = 'none';
    shown.style.display = 'flex';
}

function refresh() {
    window.location.reload(false);
}

let modal = document.querySelector(".modal");
let trigger = document.querySelector("#how-to-play");
let closeButton = document.querySelector(".close-button");

function toggleModal() {
    modal.classList.toggle("show-modal");
}

function windowOnClick(event) {
    if (event.target === modal) {
        toggleModal();
    }
}

trigger.addEventListener("click", toggleModal);
closeButton.addEventListener("click", toggleModal);
window.addEventListener("click", windowOnClick);
let giveupModal = document.querySelector('.give-up-modal');
let giveupTrigger = document.querySelector('#give-up-button');
let closeGiveup = document.querySelector(".close-button-give-up");

function toggleGiveupModal() {
    document.getElementById('secret-word').textContent += word;
    giveupModal.classList.toggle("show-modal");
}

giveupTrigger.addEventListener("click", toggleGiveupModal, false);
closeGiveup.addEventListener("click", refresh, false);
