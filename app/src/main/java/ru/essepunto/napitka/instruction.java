package ru.essepunto.napitka;

import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class instruction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        // Find the TextView and ScrollView in the layout
        TextView instructionsTextView = findViewById(R.id.instructionsTextView);
        ScrollView scrollView = findViewById(R.id.scrollView);

        // Set the text for the TextView
        instructionsTextView.setText("Всё просто.Жмём кнопку'Добавить ценники,затем нажимаем 'Скан' сканируем," +
                " можем поставить галочку 'Многократно' тогда сканер будет сам открываться автоматически после каждого" +
                " сканирования.Чтобы это остановить,жмём системную кнопку 'назад'.После того как насканировали ценников,жмём кнопку назад,и попадаем на первый экран приложения." +
                " Далее жмём кнопку 'Печать' видим сгенерированный QR код из массива SAP кодов.Далее идём в баркодерную, открываем или создаём Новый текстовый документ.Не Word,не Excel - " +
                "а именно файл Блокнота.Переключаем раскладку клавиатуры на Английскую. Далее сканируем QR код и видим как подготовленный массив вставляется в столбик в текстовый документ" +
                ", после чего выделяем всё сочетанием клавиш CTRL+A, копируем CTRL+C, и переходим в SET10.Далее Нажимаем кнопку 'Добавить списком' и вставляем туда" +
                " наши SAP коды.Жмём 'Применить' \n Готово! \n Можно нажать в приложении кнопку 'Очистить базу данных'.ВНИМАНИЕ!СКАНИРУЙТЕ В ОДИН QR КОД НЕ БОЛЕЕ 50-60 ТОВАРОВ!Это ограничение сканера Datalogic.Если больше,вы просто не сможете" +
                " считать QR.С каждым добавлением он становится сложнее.");
        instructionsTextView.setTextSize(16);
        instructionsTextView.setPadding(16, 16, 16, 16);
        instructionsTextView.setLineSpacing(1.5f, 1.8f);
        // Add the TextView to the ScrollView
    }
}
