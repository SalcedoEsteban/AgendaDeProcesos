var botonEliminar;

botonEliminar = document.getElementById("botonEliminar");

botonEliminar.addEventListener("click", alertDelete);

function alertDelete()
{
	return window.confirm("¿Esta seguro que desea Eliminar este elemento? desde confirm");
}