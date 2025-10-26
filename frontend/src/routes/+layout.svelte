<script>
  import favicon from "$lib/assets/favicon.svg";
  import "./styles.css";
  let { data, children } = $props();
  let { user, isAuthenticated } = data;
</script>

<svelte:head>
  <link rel="icon" href={favicon} />
</svelte:head>

<nav class="navbar navbar-expand-lg bg-light">
  <div class="container-fluid">
    <a class="navbar-brand" href="/">Freelancer4U</a>
    <button
      class="navbar-toggler"
      type="button"
      data-bs-toggle="collapse"
      data-bs-target="#navbarNav"
      aria-controls="navbarNav"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        {#if isAuthenticated}
          <li class="nav-item">
            <a class="nav-link" aria-current="page" href="/companies"
              >Companies</a
            >
          </li>
        {/if}
        {#if isAuthenticated}
          <li class="nav-item">
            <a class="nav-link" href="/jobs">Jobs</a>
          </li>
        {/if}
      </ul>
      <div class="d-flex">
        {#if isAuthenticated}
          <span class="navbar-text me-2">{user.name}!</span>
          <form action="/logout" method="POST" class="d-inline">
            <button class="btn btn-primary" href="/logout">Logout</button>
          </form>
        {:else}
          <a href="/login" class="btn btn-outline-primary me-2" >Login</a>
          <a href="/signup" class="btn btn-primary">Sign up</a>
        {/if}
      </div>
    </div>
  </div>
</nav>

<div class="container mt-3">
  {@render children()}
</div>
