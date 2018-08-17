function makeButton(alphabet){
    for(var l in alphabet)
    {
        var newButton=document.createElement('button');
        newButton.innerHTML=alphabet[l].l+ ":" +alphabet[l].c ;
        newButton.className+="letter-button";
        document.getElementById('letter-container').appendChild(newButton);
    }
    let buttons = document.getElementsByClassName('letter-button');
    for(let i = 0; i<buttons.length;i++){
        buttons[i].addEventListener("click",event => {alert(event.target.textContent);});
    }
    let cardButtons =document.getElementsByClassName('card-button');
    for (let i = 0; i < cardButtons.length; i++) {
        cardButtons[i].addEventListener("click", event => {alert(event.target.textContent);});
    }
}
function createANewGame(){
    fetch('http://localhost:9000/createANewGame', {
        method: "POST",
        headers: {"Content-Type": "application/json; charset=utf-8",},
        body:JSON.stringify({level:2})
    }).then (response=>response.json()).then(function (data) {
        document.getElementById('secretword').textContent=data.secretWord;
        document.getElementById('point').textContent+=data.point;
       // console.log(data.alphabet[0]);
        makeButton(data.alphabet);
        console.log(data.message);

    })
}
createANewGame();


/*createANEwGame('http://localhost:9000/createANewGame','POST','{"level":2}')
    .then (response=>response.json())
    .then(function (data) {
    document.getElementById('secretword').textContent=data.secretWord;
    document.getElementById('point').textContent+=data.point;
    // console.log(data.alphabet[0]);
    makeButton(data.alphabet);
    console.log(data.message);

}).catch(function (reject) {

});
function createANEwGame(url,method,data) {
    var request = new XMLHttpRequest();
    request.overrideMimeType('application/json');
    request.responseType = 'json';
    return new Promise(function (resolve,reject) {
       request.onreadystatechange = function () {
           if(request.readyState !== 4) return; //only run if the request is complete

           if(request.status>=200 && request.status <500){
               console.log("hgh"+request.status);
               resolve(request);
           }
           else{
               reject({
                   status: request.status,
                   statusText: request.statusText
               });
           }
       };
       request.open(method, url, true);
       request.send(data);
    });

}*/