    let deleteProfile = document.getElementById("deleteBtnProfile");
    deleteProfile.addEventListener("click",onDeleteProfile);
    let csrfHeaderNameDeleteOffer = document.head.querySelector('[name=_csrf_header]').content
    let csrfHeaderValueDeleteOffer = document.head.querySelector('[name=_csrf]').content

    async function onDeleteProfile(event) {
    let dataUsername = event.currentTarget.getAttribute('data-username');
    if (confirm("Are you sure you want to delete this user," +
        "if you do all Offers will be deleted too?")) {

// Instantiating new EasyHTTP class
    const http = new DeleteHTTP;

// Update Post
    await http.delete(`http://localhost:8080/users/profile/${dataUsername}/delete`)

    // Resolving promise for response data
    .then(() => console.log("Deleted!"))

    // Resolving promise for error
    .catch(err => console.log(err));
        sleep(1000);
        window.location.href = "http://localhost:8080/"
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

    // Return response data
    return 'resource deleted...';
}
}

    function sleep(milliseconds) {
    const start = Date.now();
    while (Date.now() - start < milliseconds);
}

