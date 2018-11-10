package us.nineworlds.serenity.jobs

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness.STRICT_STUBS
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.events.MainMenuEvent
import us.nineworlds.serenity.injection.modules.AndroidModule
import us.nineworlds.serenity.injection.modules.SerenityModule
import us.nineworlds.serenity.test.InjectingTest
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class MainMenuRetrievalJobTest : InjectingTest() {

  @Rule @JvmField public val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Inject
  lateinit var mockClient: SerenityClient

  @Mock
  lateinit var mockEventBus: EventBus

  lateinit var job: MainMenuRetrievalJob

  @Before
  override fun setUp() {
    super.setUp()
    job = MainMenuRetrievalJob()
  }

  @Test
  fun onRunFetchesCategoriesForTheSpecifiedId() {
    job.onRun()

    verify(mockClient).retrieveItemByCategories()
  }

  @Test
  fun onRunFetchesCategoriesAndPostsMainCategoryEvent() {
    job.onRun()

    verify(mockClient).retrieveItemByCategories()
    verify(mockEventBus).post(any<MainMenuEvent>())
  }

  override fun getModules(): MutableList<Any> = mutableListOf(AndroidModule(RuntimeEnvironment.application),
      TestModule())

  @Module(injects = arrayOf(MainMenuRetrievalJobTest::class),
      includes = arrayOf(SerenityModule::class, TestingModule::class),
      library = true,
      overrides = true)
  inner class TestModule {
    @Provides
    fun providesEventBus(): EventBus = mockEventBus
  }
}
