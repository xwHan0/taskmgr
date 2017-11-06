// Compiled by ClojureScript 1.9.229 {}
goog.provide('taskview.core');
goog.require('cljs.core');
goog.require('reagent.core');
if(typeof taskview.core.resume_time !== 'undefined'){
} else {
taskview.core.resume_time = reagent.core.atom.call(null,(0));
}
if(typeof taskview.core.resume_status !== 'undefined'){
} else {
taskview.core.resume_status = reagent.core.atom.call(null,new cljs.core.Keyword(null,"pause","pause",-2095325672));
}
if(typeof taskview.core.resume_handle !== 'undefined'){
} else {
taskview.core.resume_handle = reagent.core.atom.call(null,(0));
}
taskview.core.run_resume = (function taskview$core$run_resume(){
return setInterval((function (){
return cljs.core.swap_BANG_.call(null,taskview.core.resume_time,cljs.core.inc);
}),(1000));
});
taskview.core.pause_resume = (function taskview$core$pause_resume(){
return clearInterval(cljs.core.deref.call(null,taskview.core.resume_handle));
});
taskview.core.resume_componment = (function taskview$core$resume_componment(){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
if(cljs.core._EQ_.call(null,new cljs.core.Keyword(null,"pause","pause",-2095325672),cljs.core.deref.call(null,taskview.core.resume_status))){
cljs.core.reset_BANG_.call(null,taskview.core.resume_status,new cljs.core.Keyword(null,"count","count",2139924085));

return cljs.core.reset_BANG_.call(null,taskview.core.resume_handle,taskview.core.run_resume.call(null));
} else {
cljs.core.reset_BANG_.call(null,taskview.core.resume_status,new cljs.core.Keyword(null,"pause","pause",-2095325672));

return taskview.core.pause_resume.call(null);
}
})], null),[cljs.core.str(": "),cljs.core.str(cljs.core.deref.call(null,taskview.core.resume_time))].join('')], null);
});
reagent.core.render_component.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [taskview.core.resume_componment], null),document.getElementById("resume_div"));

//# sourceMappingURL=core.js.map