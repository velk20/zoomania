let deleteBtn = document.getElementById("deleteBtn");
let csrfHeaderNameDeleteOffer = document.head.querySelector('[name=_csrf_header]').content
let csrfHeaderValueDeleteOffer = document.head.querySelector('[name=_csrf]').content
deleteBtn.addEventListener("click", onDelete);
async function onDelete(event) {
    let offerId = event.currentTarget.getAttribute("data-id");
    if (confirm("Are you sure you want to delete this offer?")) {

// Instantiating new EasyHTTP class
        const http = new DeleteHTTP;

// Update Post
       await http.delete(`http://localhost:8080/offers/${offerId}/delete`)

            // Resolving promise for response data
            .then(() => console.log("Deleted!"))

            // Resolving promise for error
            .catch(err => console.log(err));

        window.location.replace("http://localhost:8080/offers/all");

    }else{
        event.preventDefault()
    }

}
// ES6 class
class DeleteHTTP {

    // Make an HTTP PUT Request
    async delete(url) {

        // Awaiting fetch which contains
        // method, headers and content-type
        const response = await fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                [csrfHeaderNameDeleteOffer]: csrfHeaderValueDeleteOffer
            }
        });

        // Awaiting for the resource to be deleted
        const resData = 'resource deleted...';

        // Return response data
        return resData;
    }
}