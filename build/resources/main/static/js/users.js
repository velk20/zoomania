const queryString = window.location.href;
const parameters = new URLSearchParams(queryString);

let pageNo = parameters.get('pageNo')
if (pageNo == null) {
    pageNo = 0;
}
let pageSize = parameters.get('pageSize')
if (pageSize == null) {
    pageSize = 8;
}
let sortBy = parameters.get('sortBy')
if (sortBy == null) {
    sortBy = 'id';
}
let sortDir = parameters.get('sortDir')
if (sortDir == null) {
    sortDir = 'asc';
}
let currentData = undefined;
let tableBody = document.getElementById("tableBody");
let paginationNav = document.getElementById("paginationNav");

fetch(`http://localhost:8080/api/users?pageSize=${pageSize}&pageNo=${pageNo}&sortBy=${sortBy}&sortDir=${sortDir}`, {
    headers: {
        "Accept": "application/json"
    }
}).then(res => res.json())
    .then(data => {
        currentData = data;
        if (data.content.length == 0) {
            tableBody.innerHTML += `
<div class="d-block justify-content-center text-center">
<h3 class="text-center justify-content-center text-danger">No more users found!</h3>
</div>
`
        }else{
            for (let user of data.content) {
                tableBody.innerHTML += `
              <tr>
                <td>
                  <img src="https://bootdey.com/img/Content/user_1.jpg" alt="">
                  <a href="#" class="user-link">${user.firstName + ' ' + user.lastName}</a>
                  <span class="user-subhead">${user.userRoles}</span>
                </td>
                <td><h6>${user.phone}</h6></td>
                <td class="text-center">
                <h6>
                ${user.active === true ? 'Yes' : 'No'}
                
</h6>
                </td>
                <td>
                  <a href="mailto:${user.email}"><h6>${user.email}</h6></a>
                </td>
                <td style="width: 20%;">
                  <a href="/users/profile/${user.username}" class="table-link text-warning">
                                            <span class="fa-stack">
                                                <i class="fa fa-square fa-stack-2x"></i>
                                                <i class="fa fa-search-plus fa-stack-1x fa-inverse"></i>
                                            </span>
                  </a>
                  <a href="/users/profile/${user.username}" class="table-link text-info">
                                            <span class="fa-stack">
                                                <i class="fa fa-square fa-stack-2x"></i>
                                                <i class="fa fa-pencil fa-stack-1x fa-inverse"></i>
                                            </span>
                  </a>
                  <a data-username="${user.username}" class="deleteUserBtn table-link danger">
                                            <span class="fa-stack">
                                                <i class="fa fa-square fa-stack-2x"></i>
                                                <i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>
                                            </span>
                  </a>
                </td>
              </tr>
            `;

            }

        }
    })
    .then(() => {
        paginationNav.innerHTML +=
            pageNavFirstLinkAsHtml(currentData,pageNo,pageSize,sortBy,sortDir);
        paginationNav.innerHTML +=
            pageNavPreviousLinkAsHtml(currentData,pageNo,pageSize,sortBy,sortDir);
        paginationNav.innerHTML +=
            pageNavNextLinkAsHtml(currentData,pageNo,pageSize,sortBy,sortDir);
        paginationNav.innerHTML +=
            pageNavLastLinkAsHtml(currentData,pageNo,pageSize,sortBy,sortDir);
        let deleteUserBtns = document.querySelectorAll(".deleteUserBtn")
        let csrfHeaderNameDeleteOffer = document.head.querySelector('[name=_csrf_header]').content
        let csrfHeaderValueDeleteOffer = document.head.querySelector('[name=_csrf]').content
        deleteUserBtns.forEach(b=>b.addEventListener("click", onDeleteUserBtn));
        async function onDeleteUserBtn(event){
            let dataUsername = event.currentTarget.getAttribute('data-username');
            if (confirm("Are you sure you want to delete this user," +
                "if you do all Offers will be deleted too?")) {

                const http = new DeleteHTTP;

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
    })
    .catch(err => console.log(err))

function pageNavFirstLinkAsHtml(currentData,pageNo, pageSize, sortBy, sortDir) {
    let commentHtml = '<nav>\n'
    commentHtml += `<ul class="pagination">\n`

    if (currentData.pageNo != 0) {
        commentHtml += `<li class="page-item">\n`
        commentHtml+=`
        <a href="/admin/users?pageSize=${pageSize}&pageNo=${0}&sortBy=${sortBy}&sortDir=${sortDir}"
                           class="page-link">First</a>
        \n`;
    }else{
        commentHtml += `<li onselectstart="return false" onpaste="return false;" onCopy="return false" onCut="return false" onDrag="return false" onDrop="return false" autocomplete=off
 class="disabled page-item">\n`
        commentHtml+=`
        <a href="javascript:void(0)"  class="page-link">First</a>
        \n`;
    }

    commentHtml += `</li>\n`
    commentHtml += `</ul>\n`
    commentHtml += '<nav>\n'

    return commentHtml
}
function pageNavPreviousLinkAsHtml(currentData,pageNo, pageSize, sortBy, sortDir) {
    let commentHtml = '<nav>\n'
    commentHtml += `<ul class="pagination">\n`

    if (currentData.pageNo > 0) {
        commentHtml += `<li class="page-item">\n`
        commentHtml+=`
        <a href="/admin/users?pageSize=${pageSize}&pageNo=${parseInt(pageNo)-1}&sortBy=${sortBy}&sortDir=${sortDir}"
                           class="page-link">Previous</a>
        \n`;
    }else{
        commentHtml += `<li onselectstart="return false" onpaste="return false;" onCopy="return false" onCut="return false" onDrag="return false" onDrop="return false" autocomplete=off
 class="disabled page-item">\n`
        commentHtml+=`
        <a href="javascript:void(0)"  class="page-link">Previous</a>
        \n`;
    }

    commentHtml += `</li>\n`
    commentHtml += `</ul>\n`
    commentHtml += '<nav>\n'

    return commentHtml
}

function pageNavNextLinkAsHtml(currentData,pageNo, pageSize, sortBy, sortDir) {
    let commentHtml = '<nav>\n'
    commentHtml += `<ul class="pagination">\n`

    if (!currentData.last) {
        commentHtml += `<li class="page-item">\n`
        commentHtml+=`
        <a href="/admin/users?pageSize=${pageSize}&pageNo=${parseInt(pageNo)+1}&sortBy=${sortBy}&sortDir=${sortDir}"
                           class="page-link">Next</a>
        \n`;
    }else{
        commentHtml += `<li onselectstart="return false" onpaste="return false;" onCopy="return false" onCut="return false" onDrag="return false" onDrop="return false" autocomplete=off
 class="disabled page-item">\n`
        commentHtml+=`
        <a href="javascript:void(0)"  class="page-link">Next</a>
        \n`;
    }

    commentHtml += `</li>\n`
    commentHtml += `</ul>\n`
    commentHtml += '<nav>\n'

    return commentHtml
}

function pageNavLastLinkAsHtml(currentData,pageNo, pageSize, sortBy, sortDir) {
    let commentHtml = '<nav>\n'
    commentHtml += `<ul class="pagination">\n`

    if (currentData.totalElements != 0) {
        if (currentData.pageNo != currentData.totalPages-1) {
            commentHtml += `<li class="page-item">\n`
            commentHtml+=`
        <a href="/admin/users?pageSize=${pageSize}&pageNo=${currentData.totalPages-1}&sortBy=${sortBy}&sortDir=${sortDir}"
                           class="page-link">Last</a>
        \n`;
        }
        else{
            commentHtml += `<li onselectstart="return false" onpaste="return false;" onCopy="return false" onCut="return false" onDrag="return false" onDrop="return false" autocomplete=off
 class="disabled page-item">\n`
            commentHtml+=`
        <a href="javascript:void(0)"  class="page-link">Last</a>
        \n`;
        }

    }
    else{
        commentHtml += `<li onselectstart="return false" onpaste="return false;" onCopy="return false" onCut="return false" onDrag="return false" onDrop="return false" autocomplete=off
 class="disabled page-item">\n`
        commentHtml+=`
        <a href="javascript:void(0)"  class="page-link">Last</a>
        \n`;
    }

    commentHtml += `</li>\n`
    commentHtml += `</ul>\n`
    commentHtml += '<nav>\n'

    return commentHtml
}
