/*$("#especialidad")
		.on(
				"change",
				function cargarTiposProcesos() {
					$
							.ajax({
								type : "get",
								url : "/cargar-tipo-proceso/"
										+ $('#especialidad').val(),
								data : {
									especialidad : $('#especialidad').val(),
								},
								dataType : 'json',
								contentType : "application/json; charset=utf-8",
								success : function(dataJson) {
									// $("#regions").empty();
									$("#tipoProceso")
											.append(
													"<option value=''> -- Seleccione tipo proceso -- </option>");
									$.each(dataJson, function(index, tipoProceso) {
										$("#tipoProceso").append(
												"<option value=" + tipoProceso.id
														+ ">" + tipoProceso.nombre
														+ "</option>");
									});
								},
								error : function(e) {
									console.log(e);
								},
							});

					return false;
				});*/

function cargarSelectTiposProceso()
{
	$.ajax({
		type : "get",
		url : "/cargar-tipo-proceso/" + $('#especialidad').val(),
		data : {
			especialidad : $('#especialidad').val(),
		},
		dataType : 'json',
		contentType : "application/json; charset=utf-8",
		success : function(dataJson) {
			$("select[name=tipoProceso]").empty();
			$("select[name=tipoProceso]").append(
					"<option value=''>---Seleccione tipo proceso---</option>");
			$.each(dataJson, function(index, item) {
				$("select[name=tipoProceso]").append(
						"<option value=" + item.id + ">" + item.nombre
								+ "</option>");
			});

		},
		error : function(e) {
			console.log(e);
		},
	});

	return false;
}

function cargarSelectJuzgados()
{
	$.ajax({
		type : "get",
		url : "/juzgado/cargar-juzgados/" + $('#especialidad').val(),
		data : {
			especialidad : $('#especialidad').val(),
		},
		dataType : 'json',
		contentType : "application/json; charset=utf-8",
		success : function(dataJson) {
			$("select[name=juzgado]").empty();
			$("select[name=juzgado]").append(
					"<option value=''>---Seleccione el Juzgado---</option>");
			$.each(dataJson, function(index, item) {
				$("select[name=juzgado]").append(
						"<option value=" + item.id + ">" + item.nombre
								+ "</option>");
			});

		},
		error : function(e) {
			console.log(e);
		},
	});

	return false;
}