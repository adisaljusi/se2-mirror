<script>
  let { data } = $props();
  let { user, isAuthenticated } = data;
</script>

<h1>Account Details</h1>
{#if isAuthenticated}
  <p><img src={user.picture} alt="" /></p>
  <p><b>Name:</b> {user.name}</p>
  <p><b>Nickname:</b> {user.nickname}</p>
  <p><b>First Name:</b> {user.given_name}</p>
  <p><b>Last Name:</b> {user.family_name}</p>
  <p><b>Email:</b> {user.email}</p>

  {#if $user.user_roles && $user.user_roles.length > 0}
    <p><b>Roles:</b> {user.user_roles.join(", ")}</p>
  {/if}
{:else}
  <p>Not logged in</p>
{/if}
