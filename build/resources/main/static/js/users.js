let tableBody = document.getElementById("tableBody");
fetch(`http://localhost:8080/api/users`, {
    headers: {
        "Accept": "application/json"
    }
}).then(res => res.json())
    .then(data => {
        for(let user of data) {
            tableBody.innerHTML += `
              <tr>
                <td>
                  <img src="https://bootdey.com/img/Content/user_1.jpg" alt="">
                  <a href="#" class="user-link">${user.firstName + ' '+user.lastName}</a>
                  <span class="user-subhead">${user.userRoles}</span>
                </td>
                <td><h6>${user.phone}</h6></td>
                <td class="text-center">
                <h6>
                ${user.active === true ? 'Yes':'No'}
                
</h6>
                </td>
                <td>
                  <a href="#"><h6>${user.email}</h6></a>
                </td>
                <td style="width: 20%;">
                  <a href="#" class="table-link text-warning">
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
                  <a href="/users/profile/${user.username}/delete" class="table-link danger">
                                            <span class="fa-stack">
                                                <i class="fa fa-square fa-stack-2x"></i>
                                                <i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>
                                            </span>
                  </a>
                </td>
              </tr>
            `
        }
    })