function addDisciplina() {
    const disciplinaSelect = document.getElementById('disciplina');
    const selectedDisciplina = disciplinaSelect.value;
    if (selectedDisciplina) {
        console.log('Disciplina adicionada:', selectedDisciplina);
    } else {
        alert('Por favor, selecione uma disciplina.');
    }
}

function calcCorContraste(bgHex){
    let r = parseInt(bgHex.substring(1, 3), 16);
    let g = parseInt(bgHex.substring(3, 5), 16);
    let b = parseInt(bgHex.substring(5, 7), 16);

    let luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255;

    return luminance > 0.5 ? "#000000" : "#FFFFFF";
}