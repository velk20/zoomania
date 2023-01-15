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

fetch(`http://localhost:8080/api/offers?pageSize=${pageSize}&pageNo=${pageNo}&sortBy=${sortBy}&sortDir=${sortDir}`, {
    headers: {
        "Accept": "application/json"
    }
}).then(res => res.json())
    .then(data => {
        currentData = data;

        if (data.content.length == 0) {
            tableBody.innerHTML += `
<div class="d-block justify-content-center text-center">
<h3 class="text-center justify-content-center text-danger">No more offers found!</h3>
</div>
`
        }else{
            for (let offer of data.content) {
                tableBody.innerHTML += `
              <tr>
                <td>
                  <img width="50px" height="50px" src="${offer.imageUrl}" alt="">
                  <a href="/offers/${offer.id}/details" class="user-link">${offer.title}</a>
                  <span class="user-subhead">Category: ${offer.category}</span>
                </td>
                <td><h6>$${parseFloat(offer.price).toFixed(2)}</h6></td>
                <td class="text-center">
                <h6>
                ${offer.breed}
                </h6>
                </td>
                <td>
                  <a href="/users/profile/${offer.sellerUsername}"><h6>${offer.sellerUsername}</h6></a>
                </td>
                <td style="width: 20%;">
                  <a href="/offers/${offer.id}/details" class="table-link text-warning">
                                            <span class="fa-stack">
                                                <i class="fa fa-square fa-stack-2x"></i>
                                                <i class="fa fa-search-plus fa-stack-1x fa-inverse"></i>
                                            </span>
                  </a>
                  <a href="/offers/${offer.id}/edit" class="table-link text-info">
                                            <span class="fa-stack">
                                                <i class="fa fa-square fa-stack-2x"></i>
                                                <i class="fa fa-pencil fa-stack-1x fa-inverse"></i>
                                            </span>
                  </a>
                  <a href="/offers/${offer.id}/delete" class="table-link danger">
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
    })
    .catch(err => console.log(err))

function pageNavFirstLinkAsHtml(currentData,pageNo, pageSize, sortBy, sortDir) {
    let commentHtml = '<nav>\n'
    commentHtml += `<ul class="pagination">\n`

    if (currentData.pageNo != 0) {
        commentHtml += `<li class="page-item">\n`
        commentHtml+=`
        <a href="/admin/offers?pageSize=${pageSize}&pageNo=${0}&sortBy=${sortBy}&sortDir=${sortDir}"
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
        <a href="/admin/offers?pageSize=${pageSize}&pageNo=${parseInt(pageNo)-1}&sortBy=${sortBy}&sortDir=${sortDir}"
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
        <a href="/admin/offers?pageSize=${pageSize}&pageNo=${parseInt(pageNo)+1}&sortBy=${sortBy}&sortDir=${sortDir}"
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

    if (currentData.pageNo != currentData.totalPages-1) {
        commentHtml += `<li class="page-item">\n`
        commentHtml+=`
        <a href="/admin/offers?pageSize=${pageSize}&pageNo=${currentData.totalPages-1}&sortBy=${sortBy}&sortDir=${sortDir}"
                           class="page-link">Last</a>
        \n`;
    }else{
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
