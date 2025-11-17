<script>
  import { enhance } from "$app/forms";
  let { data, form } = $props();

  let companies = $state(data.companies);
  let currentPage = $state(data.currentPage);
  let nrOfPages = $state(data.nrOfPages);
  const pageSize = 5; // change this value if you want more elements per page

  $effect(() => {
    companies = data.companies;
    currentPage = data.currentPage;
    nrOfPages = data.nrOfPages;
  });
</script>

<h1>Create Companies</h1>

{#if form?.success}
  <div class="alert alert-success alert-dismissible fade show" role="alert">
    Company created successfully!
  </div>
{/if}

{#if form?.error}
  <div class="alert alert-danger alert-dismissible fade show" role="alert">
    {form.error}
  </div>
{/if}

<form class="mb-5" method="POST" action="?/createCompany" use:enhance>
  <div class="row mb-3">
    <div class="col">
      <label class="form-label" for="name">Name</label>
      <input class="form-control" id="name" name="name" type="text" required />
    </div>
  </div>
  <div class="row mb-3">
    <div class="col">
      <label class="form-label" for="email">email</label>
      <input
        class="form-control"
        id="email"
        name="email"
        type="text"
        required
      />
    </div>
  </div>
  <button type="submit" class="btn btn-primary">Submit</button>
</form>

<h1>All Companies</h1>
<table class="table">
  <thead>
    <tr>
      <th scope="col">Name</th>
      <th scope="col">E-Mail</th>
      <th scope="col">Id</th>
    </tr>
  </thead>
  <tbody>
    {#each companies as company}
      <tr>
        <td>{company.name}</td>
        <td>{company.email}</td>
        <td>{company.id}</td>
      </tr>
    {/each}
  </tbody>
</table>

<nav>
  <ul class="pagination">
    {#each Array(nrOfPages) as _, i}
      <li class="page-item">
        <a
          class="page-link"
          class:active={currentPage == i + 1}
          href="/companies?pageNumber={i + 1}&pageSize={pageSize}"
        >
          {i + 1}
        </a>
      </li>
    {/each}
  </ul>
</nav>

<style>
  .page-link:focus {
    box-shadow: none;
  }
</style>
