function addHook(name,f){
  window.messages[name]=1;
  $(document).on(name,f);

}
