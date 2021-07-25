async function updateMultiplication() {
    let url = "http://localhost:8080/multiplications/random";
    let response = await fetch(url);
    let data = await response.json();

    let attemptForm = document.querySelector('#attempt-form');
    attemptForm['result-attempt'].value = "";
    attemptForm['user-alias'].value = "";

    let a = document.querySelector('.multiplication-a');
    let b = document.querySelector('.multiplication-b');

    a.textContent = data.factorA;
    b.textContent = data.factorB;
}

async function updateStats(alias) {
    let url = "http://localhost:8080/results?alias=" + alias;
    let response = await fetch(url);
    let data = await response.json();

    let statsBody = document.querySelector('#stats-body');
    statsBody.innerHTML = "";
    data.forEach((row) => {
        statsBody.innerHTML += '<tr><td>' + row.id + '</td>' +
            '<td>' + row.multiplication.factorA + ' x ' + row.multiplication.factorB + '</td>' +
            '<td>' + row.resultAttempt + '</td>' +
            '<td>' + (row.correct === true ? 'YES' : 'NO' ) +'</td></tr>';
    });
}

async function submitAttempt(event) {
    event.preventDefault();

    let a = document.querySelector('.multiplication-a').textContent;
    let b = document.querySelector('.multiplication-b').textContent;
    let form = event.target;
    let attempt = form['result-attempt'].value;
    let userAlias = form['user-alias'].value;

    let data = {
        user: {alias: userAlias},
        multiplication: {factorA: a, factorB: b},
        resultAttempt: attempt
    };

    let response = await fetch('/results', {
        method: 'POST',
        headers: {
            "Content-Type": "application/json; charset=utf-8"
        },
        body: JSON.stringify(data)
    });
    let result = await response.json();
    let resultMessage = document.querySelector('.result-message');

    if (result['correct']) {
        resultMessage.textContent = '😁';
    } else {
        resultMessage.textContent = '😡';
    }

    await updateMultiplication();
    await updateStats(data.user.alias);
}

let ready = async () => {
    await updateMultiplication();
    let attemptForm = document.querySelector('#attempt-form');
    attemptForm.addEventListener('submit', submitAttempt);
}

document.addEventListener("DOMContentLoaded", ready);
