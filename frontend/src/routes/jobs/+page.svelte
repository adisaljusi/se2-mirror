<script>
  import { enhance } from "$app/forms";
  import axios from "axios";

  let { data, form } = $props();
  let { user, isAuthenticated } = data;
  let companies = $state(data.companies);
  let jobs = $state(data.jobs);

  // Update jobs list when data changes (after successful form submission)
  $effect(() => {
    jobs = data.jobs;
  });
</script>

{#if form?.success}
  <div class="alert alert-success alert-dismissible fade show" role="alert">
    Job created successfully!
  </div>
{/if}

{#if form?.error}
  <div class="alert alert-danger alert-dismissible fade show" role="alert">
    {form.error}
  </div>
{/if}

{#if isAuthenticated && user.user_roles && user.user_roles.includes("admin")}
  <h1 class="mt-3">Create Job</h1>
  <form class="mb-5" method="POST" action="?/createJob" use:enhance>
    <div class="row mb-3">
      <div class="col">
        <label class="form-label" for="title">Title</label>
        <input
          class="form-control"
          id="title"
          name="title"
          type="text"
          required
        />
      </div>
    </div>
    <div class="row mb-3">
      <div class="col">
        <label class="form-label" for="description">Description</label>
        <input
          class="form-control"
          id="description"
          name="description"
          type="text"
          required
        />
      </div>
    </div>
    <div class="row mb-3">
      <div class="col">
        <label class="form-label" for="type">Type</label>
        <select class="form-select" id="type" name="jobType" required>
          <option value="">Select type...</option>
          <option value="OTHER">OTHER</option>
          <option value="TEST">TEST</option>
          <option value="IMPLEMENT">IMPLEMENT</option>
          <option value="REVIEW">REVIEW</option>
        </select>
      </div>
      <div class="col">
        <label class="form-label" for="earnings">Earnings</label>
        <input
          class="form-control"
          id="earnings"
          name="earnings"
          type="number"
          min="0"
          step="0.01"
          required
        />
      </div>
      <div class="col">
        <label class="form-label" for="company">Company</label>
        <select class="form-select" id="company" name="companyId" required>
          <option value="">Select company...</option>
          {#each companies as company}
            <option value={company.id}>{company.name}</option>
          {/each}
        </select>
      </div>
    </div>
    <button type="submit" class="btn btn-primary">Submit</button>
  </form>
{/if}
<h1>All Jobs</h1>
<table class="table">
  <thead>
    <tr>
      <th scope="col">Title</th>
      <th scope="col">Description</th>
      <th scope="col">Type</th>
      <th scope="col">Earnings</th>
      <th scope="col">State</th>
      <th scope="col">CompanyId</th>
    </tr>
  </thead>
  <tbody>
    {#each jobs as job}
      <tr>
        <td>{job.title}</td>
        <td>{job.description}</td>
        <td>{job.jobType}</td>
        <td>{job.earnings}</td>
        <td>{job.jobState}</td>
        <td>{job.companyId}</td>
      </tr>
    {/each}
  </tbody>
</table>
