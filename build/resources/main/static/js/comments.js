const offerId = document.getElementById('offerId').value
const commentForm = document.getElementById('commentForm')
commentForm.addEventListener("submit", handleFormSubmission)

let csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
let csrfHeaderValue = document.head.querySelector('[name=_csrf]').content

const commentContainer = document.getElementById('commentContainer')

async function handleFormSubmission(event) {
    event.preventDefault()

    const messageVal = document.getElementById('message').value
        if (messageVal.trim() === ""){
            alert("Message must not be empty!")
            return;
        }
    fetch(`http://localhost:8080/api/${offerId}/comments`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
        body: JSON.stringify({
            message: messageVal
        })
    }).then(res => res.json())
      .then(data => {
        document.getElementById('message').value = ""
          commentContainer.innerHTML += commentAsHtml(data)
      })
}

function commentAsHtml(comment) {
    let commentHtml = '<div>\n'
    commentHtml += `<h4>${comment.authorName}</h4>\n`
    commentHtml += `<p>${comment.message}</p>\n`
    commentHtml += '</div>\n'
    commentHtml += '<hr>\n'

    return commentHtml
}

fetch(`http://localhost:8080/api/${offerId}/comments`, {
    headers: {
        "Accept": "application/json"
    }
}).then(res => res.json())
    .then(data => {
        for(let comment of data) {
            commentContainer.innerHTML += commentAsHtml(comment)
        }
    })