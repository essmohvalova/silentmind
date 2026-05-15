package com.example.coursework_app.ui.onboarding.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework_app.R
import com.example.coursework_app.domain.model.user.CharacterType
import com.example.coursework_app.ui.components.AppBarSize
import com.example.coursework_app.ui.components.AppBarTitle
import com.example.coursework_app.ui.onboarding.CharacterCarouselItem
import com.example.coursework_app.ui.onboarding.CharacterCarouselItemsFactory
import com.example.coursework_app.ui.onboarding.components.CharacterCarousel

@Composable
fun CharacterSelectionScreen(
    selectedCharacter: CharacterType,
    onCharacterSelected: (CharacterType) -> Unit,
    onNextClicked: () -> Unit,
    modifier: Modifier = Modifier,
    characters: List<CharacterCarouselItem>? = null,
) {
    val carouselCharacters = characters ?: remember { CharacterCarouselItemsFactory.create() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBarTitle(
            title = stringResource(R.string.character_selection_title),
            size = AppBarSize.LARGE,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.character_selection_helper_question),
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        CharacterCarousel(
            characters = carouselCharacters,
            selectedCharacter = selectedCharacter,
            onCharacterSelected = onCharacterSelected,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNextClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(stringResource(R.string.character_selection_next), fontSize = 16.sp)
        }
    }
}
